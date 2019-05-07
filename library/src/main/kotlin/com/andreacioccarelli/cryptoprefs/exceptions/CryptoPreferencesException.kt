package com.andreacioccarelli.cryptoprefs.exceptions

/**
 * Designed and Developed by Andrea Cioccarelli
 */

internal class CryptoPreferencesException internal constructor(x: Throwable, y: String) : RuntimeException("$y $x")

