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
package com.cioccarellia.ksprefs.engines

import com.cioccarellia.ksprefs.config.model.KeySize
import com.cioccarellia.ksprefs.extensions.string
import com.cioccarellia.ksprefs.internal.ByteSizeable

@JvmInline
internal value class SymmetricKey(
    val bytes: ByteArray
) : ByteSizeable {

    fun string() = toString()

    override fun toString() = bytes.string()

    override fun byteCount() = bytes.size

    override fun bitCount() = byteCount() * 8

    fun matches(keySizeOptions: KeySize) = bytes.size == keySizeOptions.byteCount()

    fun trim(keySizeOptions: KeySize): SymmetricKey =
        SymmetricKey(
            bytes.sliceArray(
                0 until keySizeOptions.byteCount()
            )
        )
}