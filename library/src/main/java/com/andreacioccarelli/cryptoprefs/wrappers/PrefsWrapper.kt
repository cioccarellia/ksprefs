package com.andreacioccarelli.cryptoprefs.wrappers

import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper

/**
 * Created by andrea on 2018/Jun.
 * Part of the package com.andreacioccarelli.cryptoprefs.wrappers
 */

internal class PrefsWrapper: Wrapper {
    override fun encrypt(value: String) = value
    override fun decrypt(value: String) = value
}