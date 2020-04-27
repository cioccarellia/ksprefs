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
package com.cioccarellia.kspref.converter

import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

@PublishedApi
internal abstract class TypeConverter<I> {
    abstract fun transform(value: I): ByteArray
    abstract fun reify(value: ByteArray): I

    companion object {
        @PublishedApi
        internal inline fun <reified T> pick(): TypeConverter<*> = when (T::class) {
            String::class -> StringConverter()
            Boolean::class -> BooleanConverter()
            Int::class -> IntConverter()
            Long::class -> LongConverter()
            Float::class -> FloatConverter()
            Short::class -> ShortConverter()
            BigInteger::class -> BigIntConverter()
            BigDecimalConverter::class -> BigDecimalConverter()
            JSONObject::class -> JsonConverter()
            else -> StringConverter()
        }

        @PublishedApi
        internal inline fun <reified T> pickAndTransform(
            value: T
        ): ByteArray = when (T::class) {
            String::class -> StringConverter().transform(value as String)
            Boolean::class -> BooleanConverter().transform(value as Boolean)
            Int::class -> IntConverter().transform(value as Int)
            Long::class -> LongConverter().transform(value as Long)
            Float::class -> FloatConverter().transform(value as Float)
            Short::class -> ShortConverter().transform(value as Short)
            BigInteger::class -> BigIntConverter().transform(value as BigInteger)
            BigDecimalConverter::class -> BigDecimalConverter().transform(value as BigDecimal)
            JSONObject::class -> JsonConverter().transform(value as JSONObject)
            else -> StringConverter().transform(value.toString())
        }

        @PublishedApi
        @Suppress("IMPLICIT_CAST_TO_ANY")
        internal inline fun <reified T> pickAndReify(
            value: ByteArray
        ): T = when (T::class) {
            String::class -> StringConverter().reify(value)
            Boolean::class -> BooleanConverter().reify(value)
            Int::class -> IntConverter().reify(value)
            Long::class -> LongConverter().reify(value)
            Float::class -> FloatConverter().reify(value)
            Short::class -> ShortConverter().reify(value)
            BigInteger::class -> BigIntConverter().reify(value)
            BigDecimalConverter::class -> BigDecimalConverter().reify(value)
            JSONObject::class -> JsonConverter().reify(value)
            else -> StringConverter().reify(value)
        } as T

    }
}