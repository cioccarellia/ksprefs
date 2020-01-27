package com.cioccarellia.cryptoprefs

import android.content.Context
import androidx.security.crypto.MasterKeys
import com.cioccarellia.cryptoprefs.behaviour.PrefsClosureOperation
import com.cioccarellia.cryptoprefs.data.CryptoController
import com.cioccarellia.cryptoprefs.data.LockPath

open class PrefsController(val namespace: String, context: Context) {

    @Suppress("MayBeConstant")
    public object CryptoConfig {
        internal var closureOperation = PrefsClosureOperation.ASYNC_APPLY
        public var autoSave = false
    }

    public val config: CryptoConfig = CryptoConfig

    private val alias: String
        get() {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            return MasterKeys.getOrCreate(keyGenParameterSpec)
        }

    private val lockPath = LockPath(namespace, alias)

    internal val controller = CryptoController(lockPath, context)
}