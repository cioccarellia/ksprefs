package com.andreacioccarelli.cryptoprefs.exceptions

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs.exceptions
 */

internal class SecurePreferencesException internal constructor(e: Throwable, s: String) : RuntimeException("$s $e")

