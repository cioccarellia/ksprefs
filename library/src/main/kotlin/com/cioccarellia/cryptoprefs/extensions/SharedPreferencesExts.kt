package com.cioccarellia.cryptoprefs.extensions

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import com.cioccarellia.cryptoprefs.data.KeyScheme
import com.cioccarellia.cryptoprefs.data.LockPath
import com.cioccarellia.cryptoprefs.data.ValScheme

object IntrinsicSharedPrefs {

    /**
     *
     * */
    fun of(
        lockPath: LockPath,
        context: Context,
        keyEncryptionScheme: KeyScheme,
        valueEncryptionScheme: ValScheme
    ) = EncryptedSharedPreferences.create(
            lockPath.namespace, lockPath.alias, context,
            keyEncryptionScheme, valueEncryptionScheme
    )
}
