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

import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.converter.TypeConverter
import com.cioccarellia.kspref.enclosure.KspEnclosure
import com.cioccarellia.kspref.extensions.Reader
import com.cioccarellia.kspref.extensions.emptyByteArray
import com.cioccarellia.kspref.intrinsic.checkKey

internal class KspDispatcher(
    namespace: String,
    handle: Reader
) {
    private val enclosure = KspEnclosure(namespace, handle)

    internal fun expose() = enclosure.sharedReader

    private fun <T : Any> convert(
        value: T
    ) = TypeConverter.pickAndTransform(value)

    private fun <T : Any> reify(
        value: ByteArray
    ) = TypeConverter.pickAndReify<T>(value)

    internal fun <T : Any> push(
        key: String,
        value: T
    ) {
        checkKey(key)

        // Bytes for the given input value
        val pureBytes = convert(value)

        // Writes the converted bytes to the shared pre
        enclosure.write(key, pureBytes)
    }

    internal fun <T : Any> pull(
        key: String,
        default: T
    ): T {
        checkKey(key)

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

    /** no-def val */
    internal fun <T : Any> pull(
        key: String
    ): T {
        // The function accepts nullable values,
        // but crashes if one of them is actually void
        checkKey(key)

        val pureBytes = emptyByteArray()

        // Reads and passes bytes through an engine
        // which applies the required transformation
        // to it
        val returnedBytes = enclosure.read(key, pureBytes)

        // Reifies and returns the read data as an object
        // plugging it through a converter
        return reify(returnedBytes)
    }

    internal fun save(
        commitStrategy: CommitStrategy
    ) = enclosure.save(commitStrategy)

    internal fun remove(key: String) = enclosure.remove(key)
}