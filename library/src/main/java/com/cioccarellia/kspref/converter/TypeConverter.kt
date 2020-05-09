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

import com.cioccarellia.kspref.annotations.Derivative
import com.cioccarellia.kspref.annotations.Integral
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

@PublishedApi
internal abstract class TypeConverter<T> {

    @Derivative
    abstract fun transform(value: T): ByteArray

    @Integral
    abstract fun reify(value: ByteArray): T

    companion object {
        @PublishedApi
        internal fun <T : Any> pickAndTransform(
            value: T,
            kclass: KClass<out T>
        ): ByteArray = when (value::class) {
            // Strings
            String::class -> StringConverter().transform(value as String)
            CharSequence::class -> CharSequenceConverter().transform(value as CharSequence)

            // Bit fields
            Boolean::class -> BooleanConverter().transform(value as Boolean)
            ByteArray::class -> ByteArrayConverter().transform(value as ByteArray)
            Byte::class -> ByteConverter().transform(value as Byte)

            // Numbers
            Int::class -> IntConverter().transform(value as Int)
            Long::class -> LongConverter().transform(value as Long)
            Short::class -> ShortConverter().transform(value as Short)
            Float::class -> FloatConverter().transform(value as Float)
            Double::class -> DoubleConverter().transform(value as Double)
            BigInteger::class -> BigIntConverter().transform(value as BigInteger)
            BigDecimalConverter::class -> BigDecimalConverter().transform(value as BigDecimal)

            // Dates
            Date::class -> DateConverter().transform(value as Date)
            Calendar::class -> CalendarConverter().transform(value as Calendar)

            // Custom types
            JSONObject::class -> JsonConverter().transform(value as JSONObject)
            else -> UnknownTypeConverter().transform(value.toString())
        }

        @PublishedApi
        @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
        internal fun <T : Any> pickAndReify(
            value: ByteArray,
            kclass: KClass<T>
        ): T = when (kclass) {
            // Strings
            String::class -> StringConverter().reify(value)
            CharSequence::class -> CharSequenceConverter().reify(value)

            // Bit fields
            Boolean::class -> BooleanConverter().reify(value)
            ByteArray::class -> ByteArrayConverter().reify(value)
            Byte::class -> ByteConverter().reify(value)

            // Numbers
            Int::class -> IntConverter().reify(value)
            Long::class -> LongConverter().reify(value)
            Short::class -> ShortConverter().reify(value)
            Float::class -> FloatConverter().reify(value)
            Double::class -> DoubleConverter().reify(value)
            BigInteger::class -> BigIntConverter().reify(value)
            BigDecimalConverter::class -> BigDecimalConverter().reify(value)

            // Dates
            Date::class -> DateConverter().reify(value)
            Calendar::class -> CalendarConverter().reify(value)

            // Custom types
            JSONObject::class -> JsonConverter().reify(value)
            else -> UnknownTypeConverter().reify(value)
        } as T
    }
}