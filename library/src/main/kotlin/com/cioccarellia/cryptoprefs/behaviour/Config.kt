package com.cioccarellia.cryptoprefs.behaviour

import com.cioccarellia.cryptoprefs.PrefsController

enum class PrefsClosureOperation {
    ASYNC_APPLY, SYNC_COMMIT
}

fun PrefsController.CryptoConfig.setClosureOperation(operation: PrefsClosureOperation) {
    PrefsController.CryptoConfig.closureOperation = operation
}