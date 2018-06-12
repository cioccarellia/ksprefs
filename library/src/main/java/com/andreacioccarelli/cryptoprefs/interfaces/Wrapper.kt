package com.andreacioccarelli.cryptoprefs.interfaces

import android.content.SharedPreferences

/**
 * Created by andrea on 2018/Jun.
 * Part of the package com.andreacioccarelli.cryptoprefs.interfaces
 */
interface Wrapper {
    val prefReader: SharedPreferences
    val prefWriter: SharedPreferences.Editor
    fun encrypt(value: String): String
    fun decrypt(value: String): String
}