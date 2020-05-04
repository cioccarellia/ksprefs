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
package com.cioccarellia.kspref.crypto

import com.cioccarellia.kspref.config.crypto.parameters.KeyTrimmingOption
import com.cioccarellia.kspref.exception.InvalidKsPrefKeySize
import com.cioccarellia.kspref.extensions.bitCount
import com.cioccarellia.kspref.extensions.string
import com.cioccarellia.kspref.internal.ByteSizeable

inline class SymmetricKey(
    val bytes: ByteArray
) : ByteSizeable {

    fun string() = toString()

    fun requireEquals(keySizeOptions: KeyTrimmingOption) {
        if (keySizeOptions.byteCount() != bytes.size)
            throw InvalidKsPrefKeySize(
                keySizeOptions, keyBitCount = bytes.bitCount()
            )
    }

    override fun toString() = bytes.string()

    override fun byteCount() = bytes.size

    override fun bitCount() = byteCount() * 8
}