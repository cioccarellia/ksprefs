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
package com.cioccarellia.kspref.config.crypto

import com.cioccarellia.kspref.defaults.Defaults

data class KspEncryptionConfig internal constructor(
    var transformation: ByteTransformationStrategy = Defaults.TRANSFORMATION,
    var key: String? = null,
    var alias: String? = null,
    var iv: ByteArray? = null,
    var keySize: KeySizeTrimmingOption = Defaults.KEY_SIZE_TRIM_OPTION,
    var blockCipherMode: BlockCipherEncryptionMode = Defaults.BLOCK_CIPHER_ENCRYPTION_MODE
) {
    fun initPlainText() {
        this.key = null
        this.iv = null
        this.alias = null

        transformation = ByteTransformationStrategy.PLAIN_TEXT
    }

    fun initBase64() {
        this.key = null
        this.iv = null
        this.alias = null

        transformation = ByteTransformationStrategy.BASE64
    }

    fun initAesCbc(
        key: String,
        iv: ByteArray
    ) {
        this.key = key
        this.iv = iv
        this.alias = null

        transformation = ByteTransformationStrategy.AES

        keySize = Defaults.KEY_SIZE_TRIM_OPTION
        blockCipherMode = BlockCipherEncryptionMode.CBC
    }

    fun initAesEcb(
        key: String
    ) {
        this.key = key
        this.iv = null
        this.alias = null

        transformation = ByteTransformationStrategy.AES

        keySize = Defaults.KEY_SIZE_TRIM_OPTION
        blockCipherMode = BlockCipherEncryptionMode.ECB
    }


    fun initKeystore(
        alias: String
    ) {
        this.key = null
        this.iv = null
        this.alias = alias

        transformation = ByteTransformationStrategy.KEYSTORE

        keySize = KeySizeTrimmingOption.TRIM_128
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KspEncryptionConfig

        if (transformation != other.transformation) return false
        if (key != other.key) return false
        if (iv != null) {
            if (other.iv == null) return false
            if (!(iv as ByteArray).contentEquals(other.iv as ByteArray)) return false
        } else if (other.iv != null) return false
        if (keySize != other.keySize) return false
        if (blockCipherMode != other.blockCipherMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transformation.hashCode()
        result = 31 * result + (key?.hashCode() ?: 0)
        result = 31 * result + (iv?.contentHashCode() ?: 0)
        result = 31 * result + keySize.hashCode()
        result = 31 * result + blockCipherMode.hashCode()
        return result
    }
}