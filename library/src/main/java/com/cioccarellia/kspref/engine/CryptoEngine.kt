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
package com.cioccarellia.kspref.engine

import com.cioccarellia.kspref.annotations.Derivative
import com.cioccarellia.kspref.annotations.Integral
import com.cioccarellia.kspref.extensions.getOrThrowException

interface CryptoEngine {
    val algorithm: String
    val transformation: String

    @Derivative
    fun encrypt(
        input: ByteArray
    ): ByteArray

    @Integral
    fun decrypt(
        cipherText: ByteArray
    ): ByteArray

    fun <T> runSafely(
        operation: String = "",
        block: () -> T
    ): T = runCatching(block).getOrThrowException(operation)
}