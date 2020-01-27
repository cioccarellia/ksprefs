package com.cioccarellia.cryptoprefs.data

import android.content.Context
import android.content.SharedPreferences
import com.cioccarellia.cryptoprefs.extensions.IntrinsicSharedPrefs
import com.cioccarellia.cryptoprefs.extensions.toStringSet

internal class CryptoController(
    lockPath: LockPath,
    context: Context
) {
    val prefs: SharedPreferences = IntrinsicSharedPrefs.of(lockPath, context, KeyScheme.AES256_SIV, ValScheme.AES256_GCM)

    fun all(): MutableMap<String, *> = prefs.all

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T : Any> get(key: String, default: T): T = with(prefs) {
        val stringifiedDefault = default.toString()

        when (default) {
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)

            is Boolean -> getBoolean(key, default)
            is Set<*> -> getStringSet(key, default.toStringSet())
            else -> {
                getString(key, stringifiedDefault)
            }
        } as T
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun <T : Any> set(key: String, value: T): SharedPreferences.Editor = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)

            is Boolean -> putBoolean(key, value)
            is Set<*> -> putStringSet(key, value.toStringSet())
            else -> {
                putString(key, value.toString())
            }
        }
    }

    fun contains(key: String) = prefs.contains(key)

    fun remove(key: String): SharedPreferences.Editor = prefs.edit().remove(key)

    fun erase(): SharedPreferences.Editor = prefs.edit().clear()
}