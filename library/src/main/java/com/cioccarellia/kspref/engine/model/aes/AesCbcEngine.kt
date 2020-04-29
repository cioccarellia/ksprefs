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
package com.cioccarellia.kspref.engine.model.aes

import android.util.Base64
import com.cioccarellia.kspref.engine.CryptoEngine
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.Transmission
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesCbcEngine(
    val key: ByteArray,
    val keyByteCount: Int,
    val base64Flags: Int,
    val iv: ByteArray
) : Engine(), CryptoEngine {
    override val algorithm = "AES"
    override val cipherTransform = "AES/CBC/PKCS5Padding"

    override fun apply(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun revert(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    private val digest by lazy { MessageDigest.getInstance("SHA-256") }

    override fun keySpec(): SecretKeySpec = runSafely {
        val _key = digest.digest(key).copyOf(keyByteCount)
        SecretKeySpec(_key, algorithm)
    }

    override fun encrypt(
        input: ByteArray
    ): ByteArray = runSafely {
        val iv = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(cipherTransform)

        with(cipher) {
            init(Cipher.ENCRYPT_MODE, keySpec(), iv)

            Base64.encode(
                doFinal(input), base64Flags
            )
        }
    }

    override fun decrypt(
        cipherText: ByteArray
    ): ByteArray = runSafely {
        val iv = IvParameterSpec(iv)
        val keySpec = SecretKeySpec(key, algorithm)

        val cipher = Cipher.getInstance(cipherTransform)

        with(cipher) {
            init(Cipher.DECRYPT_MODE, keySpec(), iv)

            doFinal(
                Base64.decode(cipherText, base64Flags)
            )
        }
    }
}