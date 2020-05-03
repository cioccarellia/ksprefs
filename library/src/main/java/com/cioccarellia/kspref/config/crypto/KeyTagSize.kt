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

import com.cioccarellia.kspref.internal.ByteSizeble

enum class KeyTagSize : ByteSizeble {
    SIZE_96,
    SIZE_104,
    SIZE_112,
    SIZE_120,
    SIZE_128;

    override fun bitCount() = when (this) {
        SIZE_96 -> 96
        SIZE_104 -> 104
        SIZE_112 -> 112
        SIZE_120 -> 120
        SIZE_128 -> 128
    }

    override fun byteCount() = bitCount() / 8
}