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
package com.cioccarellia.ksprefs.extensions

import androidx.annotation.RestrictTo
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun Cipher.initDecryptAesGcmKeystore(
    secretKey: SecretKey,
    keyTagSizeInBits: Int
) {
    init(
        Cipher.DECRYPT_MODE,
        secretKey,
        GCMParameterSpec(keyTagSizeInBits, algorithm.toByteArray(), 0, 12)
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun Cipher.initEncryptAesGcmKeystore(
    secretKey: SecretKey,
    keyTagSizeInBits: Int
) {
    init(
        Cipher.ENCRYPT_MODE,
        secretKey,
        GCMParameterSpec(keyTagSizeInBits, algorithm.toByteArray(), 0, 12)
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun Cipher.initDecryptKeyPair(
    public: PrivateKey
) {
    init(Cipher.DECRYPT_MODE, public)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun Cipher.initEncryptKeyPair(
    private: PublicKey
) {
    init(Cipher.ENCRYPT_MODE, private)
}