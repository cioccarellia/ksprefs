package com.andreacioccarelli.cryptoprefs.wrappers

import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper

/**
 * Designed and Developed by Andrea Cioccarelli
 */

internal class PrefsWrapper: Wrapper {
    override fun encrypt(value: String) = value
    override fun decrypt(value: String) = value
}