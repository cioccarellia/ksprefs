package com.andreacioccarelli.cryptoprefs

import android.content.Context
import android.os.Bundle

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs.preferences
 */

@Suppress("unused", "RedundantVisibilityModifier")
public class CryptoPrefs(context: Context, fileName: String, key: String) {

    private var preferences = CryptoWrapper(context, Pair(fileName, key))

    public val allPrefsBundle: Bundle
        get() = preferences.getAllPreferencesBundle()

    public val allPrefsMap: Map<String, String>
        get() = preferences.getAllPreferencesMap()

    public val allPrefsList: ArrayList<Pair<String, String>>
        get() = preferences.getAllPreferencesList()


    public fun put(key: String, value: String) {
        preferences.put(key, value)
    }

    public fun put(key: String, value: Boolean) {
        preferences.put(key, value.toString())
    }

    public fun put(key: String, value: Int) {
        preferences.put(key, value.toString())
    }

    public fun put(key: String, value: Float) {
        preferences.put(key, value.toString())
    }

    public fun put(key: String, value: Double) {
        preferences.put(key, value.toString())
    }


    public fun getString(key: String, default: Any): String {
        return preferences.get(key, default)
    }

    public fun getBoolean(key: String, default: Any): Boolean {
        return preferences.get(key, default).toBoolean()
    }

    public fun getInt(key: String, default: Any): Int {
        return preferences.get(key, default).toInt()
    }

    public fun getFloat(key: String, default: Any): Float {
        return preferences.get(key, default).toFloat()
    }

    public fun getDouble(key: String, default: Any): Double {
        return preferences.get(key, default).toDouble()
    }



    public fun queue(key: String, value: Any) {
        preferences.queue(key, value)
    }

    public fun apply() {
        preferences.apply()
    }

    public fun remove(key: String) {
        preferences.remove(key)
    }

    public fun erase() {
        preferences.erase()
    }

}
