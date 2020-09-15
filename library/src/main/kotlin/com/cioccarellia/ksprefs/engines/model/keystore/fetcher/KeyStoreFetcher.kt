/**
 * Designed and developed by Andrea Cioccarelli (@cioccarellia)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cioccarellia.ksprefs.engines.model.keystore.fetcher

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.cioccarellia.ksprefs.defaults.Defaults
import com.cioccarellia.ksprefs.extensions.fetchEntry
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

internal object KeyStoreFetcher {

    @RequiresApi(Build.VERSION_CODES.M)
    fun keystore(
        keyStore: KeyStore,
        alias: String
    ): SecretKey = if (keyStore.containsAlias(alias)) {
        val entry = keyStore.fetchEntry<KeyStore.SecretKeyEntry>(alias)
        entry.secretKey
    } else {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )

        val props = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT

        keyGenerator.init(
            KeyGenParameterSpec.Builder(alias, props).run {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setRandomizedEncryptionRequired(false)
                build()
            }
        )

        keyGenerator.generateKey()
    }

    @Suppress("DEPRECATION")
    fun keyPair(
        keyStore: KeyStore,
        alias: String,
        context: Context
    ): KeyPair = if (keyStore.containsAlias(alias)) {
        val entry = keyStore.fetchEntry<KeyStore.PrivateKeyEntry>(alias)
        KeyPair(entry.certificate.publicKey, entry.privateKey)
    } else {
        val spec = KeyPairGeneratorSpec.Builder(context).setAlias(alias)
            .setSubject(X500Principal("CN=$alias"))
            .setSerialNumber(BigInteger.TEN)
            .setStartDate(Defaults.DEFAULT_RSA_KEY_DURATION.first)
            .setEndDate(Defaults.DEFAULT_RSA_KEY_DURATION.second)
            .build()

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        with(keyPairGenerator) {
            initialize(spec)
            generateKeyPair()
        }
    }
}