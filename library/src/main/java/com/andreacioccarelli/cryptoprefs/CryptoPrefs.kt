package com.andreacioccarelli.cryptoprefs

import android.content.Context
import android.os.Bundle

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs
 */

public class CryptoPrefs(context: Context, fileName: String, key: String, shouldEncrypt: Boolean = true) {

    private val preferences = CryptoWrapper(context, fileName to key, shouldEncrypt)
    
    public val allPrefsBundle: Bundle
        get() = preferences.getAllPreferencesBundle()
    
    public val allPrefsMap: Map<String, String>
        get() = preferences.getAllPreferencesMap()
    
    public val allPrefsList: ArrayList<Pair<String, String>>
        get() = preferences.getAllPreferencesList()


    /**
     * Commits the updated value to the SharedPreferences file.
     * If the key is already present the value will be overwritten,
     * else it will be created
     * 
     * @param key the key of the item that is going to be stored
     * @param value the value that is going to be stored in the file
     * */
    public fun put(key: String, value: Any) {
        preferences.put(key, value)
    }

    /**
     * Returns the String found in pair with the matching key.
     * If no key is found in the file, the default value will 
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getString(key: String, default: Any): String {
        return preferences.get(key, default)
    }


    /**
     * Returns the Boolean found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getBoolean(key: String, default: Any): Boolean {
        return preferences.get(key, default).toBoolean()
    }


    /**
     * Returns the Integer found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getInt(key: String, default: Any): Int {
        return preferences.get(key, default).toInt()
    }


    /**
     * Returns the Float found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getFloat(key: String, default: Any): Float {
        return preferences.get(key, default).toFloat()
    }


    /**
     * Returns the Double found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getDouble(key: String, default: Any): Double {
        return preferences.get(key, default).toDouble()
    }


    /**
     * Returns the Long found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getLong(key: String, default: Any): Long {
        return preferences.get(key, default).toLong()
    }

    /**
     * Returns the Short found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    public fun getShort(key: String, default: Any): Short {
        return preferences.get(key, default).toShort()
    }


    /**
     * Enqueues a modification that is kept on a volatile copy 
     * of the file, also with every eventual new modification 
     * enqueued with this function.
     * Once apply is called the queue is asynchronously written
     * on the disk and the changes are available for in-file
     * reading and writing operations
     *
     * @param key the key of the item that will be stored in a
     *            temporary location
     * @param value the value that is going to be stored in the file
     * */
    public fun queue(key: String, value: Any) {
        preferences.queue(key, value)
    }


    /**
     * Applies the queue modifications list to the file.
     * */
    public fun apply() {
        preferences.apply()
    }


    /**
     * Removes a field from the preferences file
     * 
     * @param key The key of the entry that is going to be deleted
     * */
    public fun remove(key: String) {
        preferences.remove(key)
    }


    /**
     * Erases all the preferences that have been saved in the file
     * */
    public fun erase() {
        preferences.erase()
    }
}
