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
package com.cioccarellia.kspref.dispatcher

import android.content.Context
import com.cioccarellia.kspref.intrinsic.checkKey
import com.cioccarellia.kspref.intrinsic.checkValue
import com.cioccarellia.kspref.transform.TypeConverter

class KspDispatcher(
    context: Context,
    namespace: String
) {
    @PublishedApi
    internal inline fun <reified T> pickConverter(
        value: T
    ) = TypeConverter.pickAndTransform(value)

    @PublishedApi
    internal inline fun <reified T> pickReifier(
        value: ByteArray
    ) = TypeConverter.pickAndReify<T>(value)

    inline fun <reified T> push(
        key: String,
        value: T
    ) {
        // The function accepts nullable values,
        // but crashes if one of them is actually void
        checkKey(key)
        checkValue(value)

        val bytes = pickConverter(value)
    }

    inline fun <reified T> pull(
        key: String,
        default: T
    ): T {
        // The function accepts nullable values,
        // but crashes if one of them is actually void
        checkKey(key)
        checkValue(default)

        val converter = pickReifier<T>(ByteArray(1))
        return default
    }
}