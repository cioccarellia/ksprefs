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
package com.cioccarellia.kspref.engine

import android.util.Base64
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.crypto.BlockCipherEncryptionMode
import com.cioccarellia.kspref.config.crypto.ByteTransformationStrategy
import com.cioccarellia.kspref.engine.model.aes.AesCbcEngine
import com.cioccarellia.kspref.engine.model.aes.AesEcbEngine
import com.cioccarellia.kspref.engine.model.base64.Base64Engine
import com.cioccarellia.kspref.engine.model.plaintext.PlainTextEngine
import com.cioccarellia.kspref.exception.KsPrefEngineException
import com.cioccarellia.kspref.exception.KsPrefUnsetConfigException
import com.cioccarellia.kspref.extensions.unsafeBytes
import com.cioccarellia.kspref.internal.SymmetricKey

object EnginePicker {
    private inline val config
        get() = KsPrefs.config

    private inline val cryptoConfig
        get() = KsPrefs.config.encryption

    fun select(): Engine = when (cryptoConfig.transformation) {
        ByteTransformationStrategy.PLAIN_TEXT -> PlainTextEngine()
        ByteTransformationStrategy.BASE64 -> Base64Engine(
            Base64.NO_PADDING or Base64.NO_WRAP
        )
        ByteTransformationStrategy.AES -> {
            val base64EncryptionFlags = Base64.NO_PADDING or Base64.NO_WRAP
            val keyByteCount = cryptoConfig.keySize.byteCount()

            // We assume the key is set by the user
            // since the transformation strategy is AES
            val key = SymmetricKey(
                cryptoConfig.key.unsafeBytes()
                    ?: throw KsPrefEngineException("Encryption key is unset in encryption configuration")
            )

            when (cryptoConfig.blockCipherMode) {
                BlockCipherEncryptionMode.CBC -> {
                    // We assume the IV is set by the user
                    // since the CBC encryption mode requires an IV
                    val iv = cryptoConfig.iv
                        ?: throw KsPrefUnsetConfigException("IV is unset in encryption configuration")

                    AesCbcEngine(
                        key,
                        keyByteCount = keyByteCount,
                        base64Flags = base64EncryptionFlags,
                        iv = iv
                    )
                }
                BlockCipherEncryptionMode.ECB -> {
                    AesEcbEngine(
                        key,
                        keyByteCount = keyByteCount,
                        base64Flags = base64EncryptionFlags
                    )
                }
            }
        }
    }
}