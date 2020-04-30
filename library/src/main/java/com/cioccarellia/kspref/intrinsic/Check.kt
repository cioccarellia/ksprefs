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
@file:Suppress("NOTHING_TO_INLINE")

package com.cioccarellia.kspref.intrinsic

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@PublishedApi
internal object Check {
    fun key(
      key: String
    ) = !key.contains(" ") && key.isNotBlank()

    fun <T> value(
      value: T
    ) = value != null
}

inline fun checkKey(key: String) = require(Check.key(key))

@OptIn(ExperimentalContracts::class)
inline fun <T> checkValue(value: T) {
    contract {
        returns() implies (value != null)
    }
    require(Check.value(value))
}