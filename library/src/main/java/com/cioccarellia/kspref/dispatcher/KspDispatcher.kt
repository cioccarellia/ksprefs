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
import com.cioccarellia.kspref.enclosure.KspEnclosure
import com.cioccarellia.kspref.intrinsic.checkKey
import com.cioccarellia.kspref.intrinsic.checkValue
import com.cioccarellia.kspref.transform.TypeConverter

@PublishedApi
internal class KspDispatcher(
    context: Context,
    namespace: String
) {
    @PublishedApi
    internal val enclosure = KspEnclosure(context, namespace)

    internal fun expose() = enclosure.handle

    @PublishedApi
    internal inline fun <reified T> convert(
        value: T
    ) = TypeConverter.pickAndTransform(value)

    @PublishedApi
    internal inline fun <reified T> reify(
        value: ByteArray
    ) = TypeConverter.pickAndReify<T>(value)


    @PublishedApi
    internal inline fun <reified T> push(
        key: String,
        value: T
    ) {
        // The function accepts nullable values,
        // but crashes if one of them is actually void
        checkKey(key)
        checkValue(value)

        // Bytes for the given input value
        val pureBytes = convert(value)

        // Writes the converted bytes to the shared pre
        enclosure.write(key, pureBytes)
    }

    @PublishedApi
    internal inline fun <reified T> pull(
        key: String,
        default: T
    ): T {
        // The function accepts nullable values,
        // but crashes if one of them is actually void
        checkKey(key)
        checkValue(default)

        // Bytes for the given input default value
        // plugged into a type converter
        val pureBytes = convert(default)

        // Reads and passes bytes through an engine
        // which applies the required transformation
        // to it
        val returnedBytes = enclosure.read(key, pureBytes)

        // Reifies and returns the read data as an object
        // plugging it through a converter
        return reify(returnedBytes)
    }

    internal fun delete(
        key: String
    ) = enclosure.delete(key)
}