package com.andreacioccarelli.cryptoprefs

import android.content.Context
import android.os.Bundle
import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper
import com.andreacioccarelli.cryptoprefs.wrappers.PreferencesEncrypter
import com.andreacioccarelli.cryptoprefs.wrappers.PreferencesWrapper

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs
 */

internal class CryptoWrapper(context: Context, autoPrefs: Pair<String, String>, shouldEncrypt: Boolean) {

    private val crypto: Wrapper = if (shouldEncrypt) PreferencesEncrypter(context, autoPrefs) else PreferencesWrapper(context, autoPrefs.first)

    internal fun getAllPreferencesBundle(): Bundle {
        val result = Bundle()

        crypto.prefReader.all.map {
            result.putString(crypto.decrypt(it.key), crypto.decrypt(it.value.toString()))
        }

        return result
    }

    internal fun getAllPreferencesMap(): Map<String, String> {
        val result = HashMap<String, String>()

        crypto.prefReader.all.map {
            result[crypto.decrypt(it.key)] = crypto.decrypt(it.value.toString())
        }

        return result
    }

    internal fun getAllPreferencesList(): ArrayList<Pair<String, String>> {
        val result = ArrayList<Pair<String, String>>()

        crypto.prefReader.all.map {
            result.add(crypto.decrypt(it.key) to crypto.decrypt(it.value.toString()))
        }

        return result
    }

    internal fun get(key: String, default: Any): String {
        val encryptedString = crypto.prefReader.getString(crypto.encrypt(key), crypto.encrypt(default.toString()))
        return crypto.decrypt(encryptedString)
    }


    internal fun put(key: String, value: Any) {
        crypto.prefWriter.putString(crypto.encrypt(key), crypto.encrypt(value.toString())).apply()
    }

    internal fun queue(key: String, value: Any) {
        crypto.prefWriter.putString(crypto.encrypt(key), crypto.encrypt(value.toString()))
    }

    internal fun apply() {
        crypto.prefWriter.apply()
    }

    internal fun remove(key: String) {
        crypto.prefWriter.remove(key).apply()
    }

    internal fun erase() {
        crypto.prefWriter.clear().apply()
    }
}