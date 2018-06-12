package com.andreacioccarelli.cryptoprefs.wrappers

import android.content.Context
import android.content.SharedPreferences
import com.andreacioccarelli.cryptoprefs.interfaces.Wrapper

/**
 * Created by andrea on 2018/Jun.
 * Part of the package com.andreacioccarelli.cryptoprefs.wrappers
 */

internal class PreferencesWrapper(context: Context, filename: String): Wrapper {
    override val prefReader: SharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    override val prefWriter: SharedPreferences.Editor = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()

    override fun encrypt(value: String) = value
    override fun decrypt(value: String) = value
}