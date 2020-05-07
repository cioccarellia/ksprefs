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
package com.cioccarellia.kspref.engine.secondary

import android.annotation.TargetApi
import android.os.Build
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@TargetApi(Build.VERSION_CODES.M)
internal class AesGcmSecondaryEngine(
    private val secretKey: SecretKey,
    private val keyTagSizeInBits: Int
) : SecondaryEngine() {

    val _encryption: Cipher
        get() {
            val cipher = Cipher.getInstance(AES_MODE_FOR_POST_API_23)
            cipher.apply {
                init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    GCMParameterSpec(
                        keyTagSizeInBits,
                        AES_MODE_FOR_POST_API_23.toByteArray(),
                        0,
                        12
                    )
                )
            }

            return cipher
        }

    val _decryption: Cipher
        get() {
            val cipher = Cipher.getInstance(AES_MODE_FOR_POST_API_23)
            cipher.apply {
                init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    GCMParameterSpec(
                        keyTagSizeInBits,
                        AES_MODE_FOR_POST_API_23.toByteArray(),
                        0,
                        12
                    )
                )
            }

            return cipher
        }

    override fun computeCipher(mode: Int): Cipher {
        TODO("Not yet implemented")
    }

    override fun encrypt(
        input: ByteArray
    ): ByteArray = runSafely {
        val encrypted = _encryption.doFinal(input)
        Base64.encode(encrypted, Base64.URL_SAFE)
    }

    override fun decrypt(
        cipherText: ByteArray
    ): ByteArray = runSafely {
        val decoded = Base64.decode(cipherText, Base64.URL_SAFE)
        _decryption.doFinal(decoded)
    }

    companion object {
        private const val AES_MODE_FOR_POST_API_23 = "AES/GCM/NoPadding"
    }
}