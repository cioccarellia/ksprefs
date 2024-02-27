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
package com.cioccarellia.ksprefs.dispatcher

import android.content.Context
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.cioccarellia.ksprefs.converters.TypeConverter
import com.cioccarellia.ksprefs.enclosure.KspEnclosure
import com.cioccarellia.ksprefs.intrinsic.checkKey
import kotlin.reflect.KClass


/**
 *
 * note: centralized single-threaded class, needs synchronization primitives when accessed
 *
 * */
@PublishedApi
internal class KspDispatcher(
    namespace: String,
    context: Context
) {
    private val enclosure = KspEnclosure(namespace, context)

    internal fun expose() = enclosure.sharedReader
    internal fun engine() = enclosure.engine

    private fun <T : Any> transform(
        value: T
    ) = TypeConverter.pickAndTransform(value, value::class)

    private fun <T : Any> reify(
        value: ByteArray,
        kclass: KClass<T>
    ) = TypeConverter.pickAndReify(value, kclass)

    @PublishedApi
    internal fun <T : Any> push(
        key: String,
        value: T,
        strategy: CommitStrategy
    ) {
        checkKey(key)

        // Bytes for the given input value
        val pureBytes: ByteArray = transform(value)

        // Writes the converted bytes to the shared pre
        enclosure.write(key, pureBytes, strategy)
    }

    @PublishedApi
    internal fun <T : Any> pull(
        key: String,
        fallback: T
    ): T {
        checkKey(key)

        // Bytes for the given input fallback value
        // plugged into a type converter
        val pureBytes: ByteArray = transform(fallback)

        // Reads and passes bytes through an engine
        // which applies the required transformation
        // to it
        val returnedBytes: ByteArray = enclosure.read(key, pureBytes)

        // Reifies and returns the read data as an object
        // plugging it through a converter
        return reify(returnedBytes, fallback::class)
    }

    @PublishedApi
    internal fun <T : Any> pull(
        key: String,
        kclass: KClass<T>
    ): T {
        checkKey(key)

        // Reads the value found for the exact key
        val returnedBytes: ByteArray = enclosure.readUnsafe(key)

        // Reifies and returns the read data as an object
        // plugging it through a converter
        return reify(returnedBytes, kclass)
    }

    @PublishedApi
    internal fun raw() = enclosure.all()

    @PublishedApi
    internal fun exists(
        key: String
    ) = enclosure.exists(key)

    @PublishedApi
    internal fun save(
        commitStrategy: CommitStrategy
    ) = enclosure.save(commitStrategy)

    @PublishedApi
    internal fun remove(key: String) = enclosure.remove(key)

    @PublishedApi
    internal fun clear() = enclosure.clear()
}