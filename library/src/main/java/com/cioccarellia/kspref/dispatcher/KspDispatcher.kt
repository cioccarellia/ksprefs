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
import com.cioccarellia.kspref.config.model.CommitStrategy
import com.cioccarellia.kspref.converter.TypeConverter
import com.cioccarellia.kspref.enclosure.KspEnclosure
import com.cioccarellia.kspref.intrinsic.checkKey
import kotlin.reflect.KClass

@PublishedApi
internal class KspDispatcher(
    namespace: String,
    context: Context
) {
    private val enclosure = KspEnclosure(namespace, context)

    internal fun expose() = enclosure.sharedReader

    private fun <T : Any> convert(
        value: T
    ) = TypeConverter.pickAndTransform(value)

    private fun <T : Any> reify(
        value: ByteArray,
        kclass: KClass<T>
    ) = TypeConverter.pickAndReify(value, kclass)

    internal fun <T : Any> push(
        key: String,
        value: T,
        strategy: CommitStrategy
    ) {
        checkKey(key)

        // Bytes for the given input value
        val pureBytes = convert(value)

        // Writes the converted bytes to the shared pre
        enclosure.write(key, pureBytes, strategy)
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
        return reify(returnedBytes, default::class)
    }

    @PublishedApi
    internal fun <T : Any> pull(
        key: String,
        kclass: KClass<T>
    ): T {
        checkKey(key)

        // Reads the value found for the exact key
        val returnedBytes = enclosure.readUnsafe(key)

        // Reifies and returns the read data as an object
        // plugging it through a converter
        return reify(returnedBytes, kclass)
    }

    internal fun exists(
        key: String
    ) = enclosure.exists(key)

    internal fun save(
        commitStrategy: CommitStrategy
    ) = enclosure.save(commitStrategy)

    internal fun remove(key: String) = enclosure.remove(key)
}