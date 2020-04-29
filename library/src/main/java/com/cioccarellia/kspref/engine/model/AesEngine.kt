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
package com.cioccarellia.kspref.engine.model

import android.util.Base64
import com.cioccarellia.kspref.annotations.Derivative
import com.cioccarellia.kspref.annotations.Integral
import com.cioccarellia.kspref.config.crypto.KeySizeTrimmingOption
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.Transmission
import com.cioccarellia.kspref.extensions.getOrThrowException
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesEngine(
    val key: ByteArray,
    val byteTrimming: KeySizeTrimmingOption
) : Engine() {
    private val baseEncryptionAlgorithm = "AES"
    private val cipherTransformation = "AES/ECB/PKCS5Padding"

    @Derivative
    override fun apply(incoming: Transmission) = Transmission(
        encrypt(
            incoming.payload
        )
    )

    @Integral
    override fun revert(outgoing: Transmission) = Transmission(
        decrypt(
            outgoing.payload
        )
    )

    private val digest by lazy { MessageDigest.getInstance("SHA-256") }

    private fun key() = runCatching {
        digest.digest(key).copyOf(16)
    }.getOrThrowException()

    private fun keySpec(): SecretKeySpec = runCatching {
        SecretKeySpec(key(), baseEncryptionAlgorithm)
    }.getOrThrowException()

    private fun encrypt(input: ByteArray): ByteArray = runCatching {
        val cipher = Cipher.getInstance(cipherTransformation)

        with(cipher) {
            init(Cipher.ENCRYPT_MODE, keySpec())
            Base64.encode(doFinal(input), Base64.DEFAULT)
        }
    }.getOrThrowException()

    private fun decrypt(cipherText: ByteArray): ByteArray = runCatching {
        val cipher = Cipher.getInstance(cipherTransformation)

        with(cipher) {
            init(Cipher.DECRYPT_MODE, keySpec())

            doFinal(
                Base64.decode(cipherText, Base64.DEFAULT)
            )
        }
    }.getOrThrowException()
}