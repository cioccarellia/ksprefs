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
package com.cioccarellia.ksprefs.config.model

import com.cioccarellia.ksprefs.internal.ByteSizeable

enum class KeyTagSize : ByteSizeable {
    SIZE_96_BITS,
    SIZE_104_BITS,
    SIZE_112_BITS,
    SIZE_120_BITS,
    SIZE_128_BITS;

    override fun bitCount() = when (this) {
        SIZE_96_BITS -> 96
        SIZE_104_BITS -> 104
        SIZE_112_BITS -> 112
        SIZE_120_BITS -> 120
        SIZE_128_BITS -> 128
    }

    override fun byteCount() = bitCount() / 8
}