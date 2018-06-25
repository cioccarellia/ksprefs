package com.andreacioccarelli.cryptoprefs.interfaces

import android.content.SharedPreferences

/**
 * Created by andrea on 2018/Jun.
 * Part of the package com.andreacioccarelli.cryptoprefs.interfaces
 */
interface Wrapper {

    /**
     * SharedPreferences object used to read preferences from the
     * xml file
     * */
    val prefReader: SharedPreferences

    /**
     * SharedPreferences.Editor object used to write/update values
     * */
    val prefWriter: SharedPreferences.Editor

    /**
     * Method that returns the encrypted matching string
     * */
    fun encrypt(value: String): String

    /**
     * Method that returns the decrypted matching string
     * */
    fun decrypt(value: String): String
}