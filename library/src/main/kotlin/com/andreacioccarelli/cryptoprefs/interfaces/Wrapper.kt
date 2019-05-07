package com.andreacioccarelli.cryptoprefs.interfaces

/**
 * Designed and Developed by Andrea Cioccarelli
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