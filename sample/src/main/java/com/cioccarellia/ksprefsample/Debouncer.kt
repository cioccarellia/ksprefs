package com.cioccarellia.ksprefsample

import android.view.View

internal object Debouncer {
    @Volatile
    private var enabled: Boolean = true
    private val enableAgain = Runnable { enabled = true }

    fun canPerform(view: View): Boolean {
        if (enabled) {
            enabled = false
            view.post(enableAgain)
            return true
        }
        return false
    }
}

internal fun <T : View> T.onClickDebounced(click: (view: T) -> Unit) {
    setOnClickListener {
        if (Debouncer.canPerform(it)) {
            @Suppress("UNCHECKED_CAST")
            click(it as T)
        }
    }
}
