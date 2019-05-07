package com.andreacioccarelli.cryptoprefs

import android.content.Context
import android.content.SharedPreferences
import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper
import com.andreacioccarelli.cryptoprefs.wrappers.PrefsEncrypter
import com.andreacioccarelli.cryptoprefs.wrappers.PrefsWrapper

/**
 * Designed and Developed by Andrea Cioccarelli
 */

public class CryptoWrapper(context: Context, auth: Pair<String, String>, shouldEncrypt: Boolean) {

    private val reader: SharedPreferences = context.getSharedPreferences(auth.first, Context.MODE_PRIVATE)
    private val writer: SharedPreferences.Editor = context.getSharedPreferences(auth.first, Context.MODE_PRIVATE).edit()

    private val crypto: Wrapper = if (shouldEncrypt) PrefsEncrypter(auth) else PrefsWrapper()

    fun getAll(): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()

        for (field in reader.all) {
            val key = field.key.toString()
            val value = field.value.toString()

            result[crypto.decrypt(key)] = crypto.decrypt(value)
        }

        return result.toMap()
    }

    fun get(key: String, default: Any): String {
        val encryptedString = reader.getString(crypto.encrypt(key), crypto.encrypt(default.toString()))
        return crypto.decrypt(encryptedString!!)
    }

    fun put(key: String, value: Any) {
        writer.putString(crypto.encrypt(key), crypto.encrypt(value.toString())).commit()
    }

    fun queue(key: String, value: Any) {
        writer.putString(crypto.encrypt(key), crypto.encrypt(value.toString()))
    }

    fun remove(key: String) {
        writer.remove(crypto.encrypt(key)).commit()
    }

    fun apply() = writer.commit()

    fun erase() = writer.clear().commit()
}