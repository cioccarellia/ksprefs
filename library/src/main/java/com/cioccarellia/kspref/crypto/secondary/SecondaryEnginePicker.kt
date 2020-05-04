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
package com.cioccarellia.kspref.crypto.secondary

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.cioccarellia.kspref.defaults.Defaults
import com.cioccarellia.kspref.exception.SafeRun
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.security.auth.x500.X500Principal


object SecondaryEnginePicker : SafeRun {

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    fun select(
        alias: String,
        context: Context,
        keyStore: KeyStore,
        keyTagSizeInBits: Int
    ): SecondaryEngine = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            aesKeyValueEngine(alias, keyStore, keyTagSizeInBits)
        }
        else -> {
            rsaKeyValueEngine(alias, context, keyStore)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun aesKeyValueEngine(
        alias: String,
        keyStore: KeyStore,
        keyTagSizeInBits: Int
    ): AesGcmSecondaryEngine = runSafely {
        val secretKey = if (keyStore.containsAlias(alias)) {
            // KeyStore already contains an entry for this alias
            val entry = keyStore.fetchEntry<KeyStore.SecretKeyEntry>(alias)
            entry.secretKey
        } else {
            // First time initializing this alias
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

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

        AesGcmSecondaryEngine(secretKey, keyTagSizeInBits)
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun rsaKeyValueEngine(
        alias: String,
        context: Context,
        keyStore: KeyStore
    ): RsaKeyPairSecondaryEngine = runSafely {
        val keyPair = if (keyStore.containsAlias(alias)) {
            // KeyStore already contains an entry for this alias
            val entry = keyStore.fetchEntry<KeyStore.PrivateKeyEntry>(alias)
            KeyPair(entry.certificate.publicKey, entry.privateKey)
        } else {
            // First time initializing this alias
            val spec = KeyPairGeneratorSpec.Builder(context).setAlias(alias)
                .setSubject(X500Principal("CN=$alias"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(Defaults.DEFAULT_RSA_KEY_DURATION.first)
                .setEndDate(Defaults.DEFAULT_RSA_KEY_DURATION.second)
                .build()

            val keyPairGenerator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE)

            with(keyPairGenerator) {
                initialize(spec)
                generateKeyPair()
            }
        }

        RsaKeyPairSecondaryEngine(keyPair)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : KeyStore.Entry> KeyStore.fetchEntry(
        alias: String
    ) = getEntry(alias, null) as T
}