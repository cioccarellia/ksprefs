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
package com.cioccarellia.ksprefs.engine.model.keystore

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import com.cioccarellia.ksprefs.engine.CryptoEngine
import com.cioccarellia.ksprefs.engine.Engine
import com.cioccarellia.ksprefs.engine.Transmission
import com.cioccarellia.ksprefs.extensions.initDecryptKeyPair
import com.cioccarellia.ksprefs.extensions.initEncryptKeyPair
import java.security.KeyPair
import java.security.KeyStore
import javax.crypto.Cipher

internal class RsaKeyPairKeyStoreEngine(
    context: Context,
    alias: String,
    val base64Flags: Int
) : Engine(), CryptoEngine {

    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val fullAlgorithm = "RSA/ECB/PKCS1Padding"

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).also {
        it.load(null)
    }

    @SuppressLint("NewApi")
    val keyPair: KeyPair = KeyStoreFetcher.keyPair(keyStore, alias, context)

    private val encryptionCipher: Cipher
        get() = Cipher.getInstance(fullAlgorithm).apply {
            initEncryptKeyPair(keyPair.public)
        }

    private val decryptionCipher: Cipher
        get() = Cipher.getInstance(fullAlgorithm).apply {
            initDecryptKeyPair(keyPair.private)
        }

    override fun derive(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun integrate(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    override fun encrypt(input: ByteArray): ByteArray = runSafely {
        Base64.encode(
            encryptionCipher.doFinal(input),
            base64Flags
        )
    }

    override fun decrypt(cipherText: ByteArray): ByteArray = runSafely {
        decryptionCipher.doFinal(
            Base64.decode(cipherText, base64Flags)
        )
    }
}