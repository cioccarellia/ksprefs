package com.cioccarellia.cryptoprefs.data

import android.content.SharedPreferences
import com.cioccarellia.cryptoprefs.PrefsController
import com.cioccarellia.cryptoprefs.behaviour.PrefsClosureOperation

fun SharedPreferences.Editor.auto() {
    when (PrefsController.CryptoConfig.closureOperation) {
        PrefsClosureOperation.ASYNC_APPLY -> apply()
        PrefsClosureOperation.SYNC_COMMIT -> commit()
    }
}

fun SharedPreferences.Editor.closure() {
    if (PrefsController.CryptoConfig.autoSave) auto()
}