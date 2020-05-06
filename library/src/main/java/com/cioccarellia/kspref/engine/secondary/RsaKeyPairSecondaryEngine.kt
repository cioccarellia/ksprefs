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

import android.util.Base64
import java.security.KeyPair
import javax.crypto.Cipher

class RsaKeyPairSecondaryEngine(
    private val keyPair: KeyPair
) : SecondaryEngine() {

    override fun computeCipher(mode: Int): Cipher = runSafely {
        val cipher: Cipher = Cipher.getInstance(RSA_MODE)
        val key = if (mode == Cipher.DECRYPT_MODE) keyPair.public else keyPair.private

        cipher.apply {
            init(mode, key)
        }
    }

    override fun encrypt(input: ByteArray): ByteArray = runSafely {
        val cipher = computeCipher(Cipher.ENCRYPT_MODE)
        val encrypted = cipher.doFinal(input)

        Base64.encode(encrypted, Base64.URL_SAFE)
    }

    override fun decrypt(cipherText: ByteArray): ByteArray = runSafely {
        val cipher = computeCipher(Cipher.DECRYPT_MODE)
        val decoded = Base64.decode(cipherText, Base64.URL_SAFE)

        cipher.doFinal(decoded)
    }

    companion object {
        private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
    }
}