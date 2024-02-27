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
package com.cioccarellia.ksprefs.engines

import android.content.Context
import android.os.Build
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.engines.base.Engine
import com.cioccarellia.ksprefs.engines.model.aes.AesCbcEngine
import com.cioccarellia.ksprefs.engines.model.aes.AesEcbEngine
import com.cioccarellia.ksprefs.engines.model.base64.Base64Engine
import com.cioccarellia.ksprefs.engines.model.keystore.AesKeyStoreEngine
import com.cioccarellia.ksprefs.engines.model.keystore.RsaKeyPairKeyStoreEngine
import com.cioccarellia.ksprefs.engines.model.plaintext.PlainTextEngine
import com.cioccarellia.ksprefs.extensions.toSymmetricKey

internal object EncryptionSelector {
    private val config = KsPrefs.config.encryptionType

    fun selectEngine(
        context: Context
    ): Engine = when (config) {
        is EncryptionType.PlainText -> PlainTextEngine()
        is EncryptionType.Base64 -> Base64Engine(config.flags)
        is EncryptionType.AesEcb -> {
            val key = EncryptionKeyChecker.approve(
                config.key.toSymmetricKey(), config.keySize
            )

            AesEcbEngine(
                key,
                keyByteCount = key.byteCount(),
                base64Flags = config.base64Flags
            )
        }
        is EncryptionType.AesCbc -> {
            val key = EncryptionKeyChecker.approve(
                config.key.toSymmetricKey(), config.keySize
            )

            AesCbcEngine(
                key,
                keyByteCount = key.byteCount(),
                base64Flags = config.base64Flags,
                iv = config.iv
            )
        }
        is EncryptionType.KeyStore -> {
            val alias = config.alias

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AesKeyStoreEngine(
                    alias,
                    keyTagSizeInBits = config.keyTagSize.bitCount(),
                    base64Flags = config.base64Flags
                )
            } else {
                RsaKeyPairKeyStoreEngine(
                    context,
                    alias,
                    base64Flags = config.base64Flags
                )
            }
        }
    }
}