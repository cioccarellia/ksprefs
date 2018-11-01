package com.andreacioccarelli.cryptoprefs.interfaces

/**
 * Created by andrea on 2018/Jun.
 * Part of the package com.andreacioccarelli.cryptoprefs.interfaces
 */
interface Wrapper {
    /**
     * Method that returns the encrypted matching string
     * */
    fun encrypt(value: String): String

    /**
     * Method that returns the decrypted matching string
     * */
    fun decrypt(value: String): String
}