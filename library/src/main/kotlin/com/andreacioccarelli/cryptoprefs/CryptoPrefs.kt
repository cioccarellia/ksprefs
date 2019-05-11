@file:Suppress("MemberVisibilityCanBePrivate")

package com.andreacioccarelli.cryptoprefs

import android.content.Context

/**
 * Designed and Developed by Andrea Cioccarelli
 */

class CryptoPrefs(context: Context, fileName: String, key: String, shouldEncrypt: Boolean = true) {

    internal val preferences = CryptoWrapper(context, fileName to key, shouldEncrypt)

    /**
     * Returns the whole file content in a map
     * */
    fun getAll(): Map<String, String> = preferences.getAll()


    /**
     * Commits the updated value to the SharedPreferences file.
     * If the key is already present the value will be overwritten,
     * else it will be created
     *
     * @param key the key of the item that is going to be stored
     * @param value the value that is going to be stored in the file
     * */
    fun put(key: String, value: Any) {
        preferences.put(key, value)
    }


    /**
     * Returns the value found in pair with the matching key.
     * If no key is found in the file, the default value will
     * be returned and then, a field containing the key and the
     * given default value will be created on the preferences.
     *
     * @param key the key of the item that will be searched
     * @param default the default value, in case the key doesn't
     *                exists in the file
     * */
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T : Any> get(key: String, default: T) = when (default::class) {
        String::class ->    preferences.get(key, default)
        Boolean::class ->   preferences.get(key, default).toBoolean()
        Int::class ->       preferences.get(key, default).toInt()
        Float::class ->     preferences.get(key, default).toFloat()
        Long::class ->      preferences.get(key, default).toLong()
        Double::class ->    preferences.get(key, default).toDouble()
        Short::class ->     preferences.get(key, default).toShort()
        Byte::class ->      preferences.get(key, default).toByte()
        else -> throw IllegalStateException("Cannot cast value found in key {[$key] -> [${preferences.get(key, default)}]} to [${default::class.java.simpleName}]. Create your own extension function to parse it properly")
    } as T


    /**
     * Enqueues a modification that is kept on a volatile copy
     * of the file, also with every eventual new modification
     * enqueued with this function.
     * Once apply is called the enqueue is asynchronously written
     * on the disk and the changes are available for in-file
     * reading and writing operations
     *
     * @param key the key of the item that will be stored in a
     *            temporary location
     * @param value the value that is going to be stored in the file
     * */
    fun enqueue(key: String, value: Any) = preferences.queue(key, value)


    /**
     * Applies the enqueue modifications list to the file.
     * */
    fun apply() = preferences.apply()


    /**
     * Removes a field from the preferences file
     *
     * @param key The key of the entry that is going to be deleted
     * */
    fun remove(key: String) = preferences.remove(key)


    /**
     * Erases all the preferences that have been saved in the file
     * */
    fun erase() = preferences.erase()
}
