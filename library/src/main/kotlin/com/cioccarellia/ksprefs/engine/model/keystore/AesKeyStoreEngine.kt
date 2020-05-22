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

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.cioccarellia.ksprefs.engine.CryptoEngine
import com.cioccarellia.ksprefs.engine.Engine
import com.cioccarellia.ksprefs.engine.Transmission
import com.cioccarellia.ksprefs.extensions.initDecryptAesGcmKeystore
import com.cioccarellia.ksprefs.extensions.initEncryptAesGcmKeystore
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.M)
internal class AesKeyStoreEngine(
    alias: String,
    private val keyTagSizeInBits: Int,
    private val base64Flags: Int
) : Engine(), CryptoEngine {

    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val fullAlgorithm = "AES/GCM/NoPadding"

    override fun derive(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun integrate(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).also {
        it.load(null)
    }

    private val secretKey: SecretKey = KeyStoreFetcher.keystore(keyStore, alias)

    private val encryptionCipher: Cipher
        get() = Cipher.getInstance(fullAlgorithm).apply {
            initEncryptAesGcmKeystore(secretKey, keyTagSizeInBits)
        }

    private val decryptionCipher: Cipher = Cipher.getInstance(fullAlgorithm).apply {
        initDecryptAesGcmKeystore(secretKey, keyTagSizeInBits)
    }

    override fun encrypt(input: ByteArray): ByteArray = runSafely {
        Base64.encode(
            encryptionCipher.doFinal(input), base64Flags
        )
    }

    override fun decrypt(cipherText: ByteArray): ByteArray = runSafely {
        decryptionCipher.doFinal(
            Base64.decode(
                cipherText, base64Flags
            )
        )
    }
}