package com.andreacioccarelli.cryptoprefs.wrappers

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.andreacioccarelli.cryptoprefs.constants.CryptoConstants.algorithm
import com.andreacioccarelli.cryptoprefs.constants.CryptoConstants.charset
import com.andreacioccarelli.cryptoprefs.constants.CryptoConstants.transformation
import com.andreacioccarelli.cryptoprefs.exceptions.SecurePreferencesException
import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.KeyException
import java.security.MessageDigest
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs.wrappers
 */

internal class PreferencesEncrypter(context: Context, auto: Pair<String, String>) : Wrapper {

    private var writer: Cipher
    private var reader: Cipher
    private var keyCrypt: Cipher

    override val prefReader: SharedPreferences = context.getSharedPreferences(auto.first, Context.MODE_PRIVATE)
    override val prefWriter: SharedPreferences.Editor = context.getSharedPreferences(auto.first, Context.MODE_PRIVATE).edit()

    init {
        try {
            if (auto.second.isEmpty()) throw IllegalStateException("Encryption key length is 0")

            writer = Cipher.getInstance(transformation)
            reader = Cipher.getInstance(transformation)
            keyCrypt = Cipher.getInstance(transformation)

            initializeCiphers(auto.second)
        } catch (e: GeneralSecurityException) {
            throw SecurePreferencesException(e, "Error while initializing the preferences ciphers keys")
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e, "Error while initializing the preferences ciphers, unsupported charset.")
        } catch (e: KeyException) {
            throw SecurePreferencesException(e, "Error while initializing the preferences ciphers.")
        }
    }

    private fun initializeCiphers(key: String) {
        val ivSpec = iv
        val secretKey = getSecretKey(key)

        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        keyCrypt.init(Cipher.ENCRYPT_MODE, secretKey)
    }

    private fun getSecretKey(key: String): SecretKeySpec {
        val md = MessageDigest.getInstance(algorithm)
        md.reset()
        val keyBytes = md.digest(key.toByteArray(charset(charset)))

        return SecretKeySpec(keyBytes, transformation)
    }

    private val iv: IvParameterSpec
        get() {
            val iv = ByteArray(writer.blockSize)
            java.lang.System.arraycopy("abcdefghijklmnopqrstsvwxyz123456789".toByteArray(), 0, iv, 0, writer.blockSize)
            return IvParameterSpec(iv)
        }


    override fun encrypt(value: String): String {
        val encodedValue: ByteArray

        try {
            encodedValue = finalize(writer, value.toByteArray(charset(charset)))
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e, "Error while initializing the encryption ciphers, unsupported charset.")
        }

        return Base64.encodeToString(encodedValue, Base64.NO_WRAP)
    }


    override fun decrypt(value: String): String {
        val encodedValue = Base64.decode(value, Base64.NO_WRAP)
        val finalized = finalize(reader, encodedValue)

        return try {
            String(finalized, Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e, "Error while initializing the decryption ciphers, unsupported charset.")
        }
    }

    private fun finalize(finalizer: Cipher, input: ByteArray): ByteArray {
        try {
            return finalizer.doFinal(input)
        } catch (e: IllegalStateException) {
            throw SecurePreferencesException(e, "Cipher is not initialized.")
        } catch (e: IllegalBlockSizeException) {
            throw SecurePreferencesException(e, "Cipher is without padding, you are probably attempting to a file that is in plain/text format.")
        } catch (e: BadPaddingException) {
            throw SecurePreferencesException(e, "Cipher decryption data is with a wrong padding.")
        }
    }
}