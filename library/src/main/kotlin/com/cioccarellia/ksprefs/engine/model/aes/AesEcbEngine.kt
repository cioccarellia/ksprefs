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
package com.cioccarellia.ksprefs.engine.model.aes

import android.annotation.SuppressLint
import android.util.Base64
import com.cioccarellia.ksprefs.engine.CryptoEngine
import com.cioccarellia.ksprefs.engine.Engine
import com.cioccarellia.ksprefs.engine.SymmetricKey
import com.cioccarellia.ksprefs.engine.Transmission
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@SuppressLint("GetInstance")
internal class AesEcbEngine(
    val key: SymmetricKey,
    val keyByteCount: Int,
    val base64Flags: Int
) : Engine(), CryptoEngine {
    private val algorithm = "AES"
    private val cipherTransformation = "AES/ECB/PKCS5Padding"

    override fun derive(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun integrate(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    private val digest by lazy { MessageDigest.getInstance("SHA-256") }

    private fun keySpec(): SecretKeySpec = runSafely {
        val _key = digest.digest(key.bytes).copyOf(keyByteCount)
        SecretKeySpec(_key, algorithm)
    }

    override fun encrypt(input: ByteArray): ByteArray = runSafely {
        val cipher = Cipher.getInstance(cipherTransformation)

        with(cipher) {
            init(Cipher.ENCRYPT_MODE, keySpec())

            Base64.encode(
                doFinal(input), base64Flags
            )
        }
    }

    override fun decrypt(cipherText: ByteArray): ByteArray = runSafely {
        val cipher = Cipher.getInstance(cipherTransformation)

        with(cipher) {
            init(Cipher.DECRYPT_MODE, keySpec())

            doFinal(
                Base64.decode(cipherText, base64Flags)
            )
        }
    }
}