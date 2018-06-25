package com.andreacioccarelli.cryptoprefs.exceptions

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs.exceptions
 */

internal class SecurePreferencesException internal constructor(x: Throwable, y: String) : RuntimeException("$y $x")

