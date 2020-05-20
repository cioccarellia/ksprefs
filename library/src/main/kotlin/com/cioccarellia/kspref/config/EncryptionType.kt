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
package com.cioccarellia.kspref.config

import androidx.annotation.IntRange
import com.cioccarellia.kspref.annotations.PreferredForEncryption
import com.cioccarellia.kspref.config.model.KeySizeCheck
import com.cioccarellia.kspref.config.model.KeyTagSize
import com.cioccarellia.kspref.defaults.Defaults

/**
 * Defines the encryption engine to be used while
 * pushing data to/from the storage.
 * */
sealed class EncryptionType {
    /**
     * Default transformation, applies no transformation
     * to yhe bytes transferred to/from the storage.
     * */
    class PlainText : EncryptionType()

    /**
     * Applies a Base64 transformation to the bytes
     * transferred to/from the storage.
     * */
    class Base64(
        @IntRange(from = 0x0, to = 0x1F)
        val flags: Int = Defaults.DEFAULT_BASE64_FLAGS
    ) : EncryptionType()

    /**
     * Encrypts and Decrypts the bytes transferred to/from
     * the storage with "AES/ECB/PKCS5Padding" as transformation.
     * */
    @PreferredForEncryption
    class AesEcb(
        val key: String,
        val keySize: KeySizeCheck,
        @IntRange(from = 0x0, to = 0x1F)
        val base64Flags: Int = Defaults.DEFAULT_BASE64_FLAGS
    ) : EncryptionType()

    /**
     * Encrypts and Decrypts the bytes transferred to/from
     * the storage with "AES/CBC/PKCS5Padding" as transformation.
     * */
    class AesCbc(
        val key: String,
        val keySize: KeySizeCheck,
        val iv: ByteArray,
        @IntRange(from = 0x0, to = 0x1F)
        val base64Flags: Int = Defaults.DEFAULT_BASE64_FLAGS
    ) : EncryptionType() {
        init {
            require(iv.size == 16) {
                "IV length for AES Cbc must be 16 bytes (128 bit array)"
            }
        }
    }

    /**
     * Encrypts and Decrypts the bytes using the default Android Keystore
     * */
    class KeyStore(
        val alias: String,
        val keyTagSize: KeyTagSize = Defaults.KEY_TAG_SIZE,
        @IntRange(from = 0x0, to = 0x1F)
        val base64Flags: Int = Defaults.DEFAULT_BASE64_FLAGS
    ) : EncryptionType()
}
