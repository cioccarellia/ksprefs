package com.andreacioccarelli.cryptoprefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper
import com.andreacioccarelli.cryptoprefs.wrappers.PrefsEncrypter
import com.andreacioccarelli.cryptoprefs.wrappers.PrefsWrapper

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs
 */

internal class CryptoWrapper(context: Context, auth: Pair<String, String>, shouldEncrypt: Boolean) {

    private val reader: SharedPreferences = context.getSharedPreferences(auth.first, Context.MODE_PRIVATE)
    private val writer: SharedPreferences.Editor = context.getSharedPreferences(auth.first, Context.MODE_PRIVATE).edit()

    private val crypto: Wrapper = if (shouldEncrypt) PrefsEncrypter(auth) else PrefsWrapper()

    internal fun getAllPreferencesBundle(): Bundle {
        val result = Bundle()

        reader.all.map {
            result.putString(crypto.decrypt(it.key), crypto.decrypt(it.value.toString()))
        }

        return result
    }

    internal fun getAllPreferencesMap(): Map<String, String> {
        val result = HashMap<String, String>()

        reader.all.map {
            result[crypto.decrypt(it.key)] = crypto.decrypt(it.value.toString())
        }

        return result
    }

    internal fun getAllPreferencesList(): MutableList<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()

        reader.all.map {
            result.add(crypto.decrypt(it.key) to crypto.decrypt(it.value.toString()))
        }

        return result
    }

    internal fun get(key: String, default: Any): String {
        val encryptedString = reader.getString(crypto.encrypt(key), crypto.encrypt(default.toString()))
        return crypto.decrypt(encryptedString!!)
    }


    internal fun put(key: String, value: Any) {
        writer.putString(crypto.encrypt(key), crypto.encrypt(value.toString())).apply()
    }

    internal fun queue(key: String, value: Any) {
        writer.putString(crypto.encrypt(key), crypto.encrypt(value.toString()))
    }

    internal fun apply() {
        writer.apply()
    }

    internal fun remove(key: String) {
        writer.remove(key).apply()
    }

    internal fun erase() {
        writer.clear().apply()
    }
}