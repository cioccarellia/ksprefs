package com.andreacioccarelli.cryptoprefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
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
 * Part of the package com.andreacioccarelli.cryptoprefs.preferences
 */

@SuppressLint("CommitPrefEdits")
internal class PreferencesEncrypter(context: Context, auto: Pair<String, String>) {

    private val transformation = "AES/CBC/PKCS5Padding"
    private val algorithm = "SHA-256"
    private val charset = "UTF-8"

    private var writer: Cipher
    private var reader: Cipher
    private var keyCrypt: Cipher

    var prefReader: SharedPreferences
    var prefWriter: SharedPreferences.Editor

    init {
        try {
            writer = Cipher.getInstance(transformation)
            reader = Cipher.getInstance(transformation)
            keyCrypt = Cipher.getInstance(transformation)

            prefReader = context.getSharedPreferences(auto.first, Context.MODE_PRIVATE)
            prefWriter = context.getSharedPreferences(auto.first, Context.MODE_PRIVATE).edit()

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


    internal fun encrypt(value: String): String {
        val encodedValue: ByteArray

        try {
            encodedValue = finalize(writer, value.toByteArray(charset(charset)))
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e, "Error while initializing the encryption ciphers, unsupported charset.")
        }

        return Base64.encodeToString(encodedValue, Base64.NO_WRAP)
    }


    internal fun decrypt(securedEncodedValue: String): String {
        val encodedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP)
        val value = finalize(reader, encodedValue)

        return try {
            String(value, Charsets.UTF_8)
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
            throw SecurePreferencesException(e, "Cipher is without padding.")
        } catch (e: BadPaddingException) {
            throw SecurePreferencesException(e, "Cipher decryption data is with a wrong padding.")
        }
    }
}