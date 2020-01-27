package com.cioccarellia.cryptoprefs

import android.content.Context
import android.content.SharedPreferences
import com.cioccarellia.cryptoprefs.behaviour.CRUD
import com.cioccarellia.cryptoprefs.data.auto
import com.cioccarellia.cryptoprefs.data.closure

class CryptoPrefs(namespace: String, context: Context) : PrefsController(namespace, context), CRUD {

    public val prefs: SharedPreferences
        get() = controller.prefs

    /**
     * Automatically decides whether to call
     * [apply] or [commit] basing on the context
     * */
    public fun auto() = prefs.edit().auto()

    public fun commit() = prefs.edit().commit()

    public fun apply() = prefs.edit().apply()
    
    /**
     * Reading functions
     * */
    public override fun <T: Any> get(key: String, default: T)
            = controller.get(key, default)

    public override fun contains(key: String): Boolean
            = controller.contains(key)

    public override val all
        get() = controller.all()


    /**
     * Writing functions
     * */
    public override fun <T: Any> set(key: String, value: T) {
        controller.set(key, value).closure()
    }

    public override fun remove(key: String) {
        controller.remove(key).closure()
    }

    public override fun erase() {
        controller.erase().apply()
    }

}