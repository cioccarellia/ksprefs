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
import android.os.Build
import android.util.Base64
import androidx.annotation.IntRange
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@TargetApi(Build.VERSION_CODES.M)
class AesSecurityKey(
    private val secretKey: SecretKey
) : SecondaryEngine() {

    override fun computeCipher(
        @IntRange(from = 1, to = 4) mode: Int
    ): Cipher = runSafely {
        val cipher = Cipher.getInstance(AES_MODE_FOR_POST_API_23)
        cipher.apply {
            init(
                mode,
                secretKey,
                GCMParameterSpec(128, AES_MODE_FOR_POST_API_23.toByteArray(), 0, 12)
            )
        }
    }

    override fun encrypt(
        input: ByteArray
    ): ByteArray = runSafely {
        val cipher = computeCipher(Cipher.ENCRYPT_MODE)
        val encrypted = cipher.doFinal(input)

        Base64.encode(encrypted, Base64.URL_SAFE)
    }

    override fun decrypt(
        cipherText: ByteArray
    ): ByteArray = runSafely {
        val cipher = computeCipher(Cipher.DECRYPT_MODE)
        val decoded = Base64.decode(cipherText, Base64.URL_SAFE)

        cipher.doFinal(decoded)
    }

    companion object {
        private const val AES_MODE_FOR_POST_API_23 = "AES/GCM/NoPadding"
    }
}