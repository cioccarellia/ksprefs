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
package com.cioccarellia.ksprefs.converters

import com.cioccarellia.ksprefs.annotations.Derivative
import com.cioccarellia.ksprefs.annotations.Integral
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

@PublishedApi
internal abstract class TypeConverter<T> {

    @Derivative
    abstract fun derive(value: T): ByteArray

    @Integral
    abstract fun integrate(value: ByteArray): T

    companion object {
        @Suppress("UNCHECKED_CAST")
        internal fun <T : Any> pickAndTransform(
            value: T,
            kclass: KClass<out T>
        ): ByteArray = when {
            // Strings
            value is String -> StringConverter().derive(value as String)
            value is CharSequence -> CharSequenceConverter().derive(value as CharSequence)

            // Bit fields
            value is Boolean -> BooleanConverter().derive(value as Boolean)
            value is ByteArray -> ByteArrayConverter().derive(value as ByteArray)
            value is Byte -> ByteConverter().derive(value as Byte)

            // Numbers
            value is Int -> IntConverter().derive(value as Int)
            value is Long -> LongConverter().derive(value as Long)
            value is Short -> ShortConverter().derive(value as Short)
            value is Float -> FloatConverter().derive(value as Float)
            value is Double -> DoubleConverter().derive(value as Double)
            value is BigInteger -> BigIntConverter().derive(value as BigInteger)
            value is BigDecimalConverter -> BigDecimalConverter().derive(value as BigDecimal)

            // Dates
            value is Date -> DateConverter().derive(value as Date)
            value is Calendar -> CalendarConverter().derive(value as Calendar)

            // Custom types
            value is JSONObject -> JsonConverter().derive(value as JSONObject)

            // Enums
            kclass.java.isEnum -> EnumConverter(kclass as KClass<Enum<*>>).derive(value as Enum<*>)
            else -> UnknownTypeConverter().derive(value.toString())
        }

        @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
        internal fun <T : Any> pickAndReify(
            value: ByteArray,
            kclass: KClass<T>
        ): T = when {
            // Strings
            kclass == String::class -> StringConverter().integrate(value)
            kclass == CharSequence::class -> CharSequenceConverter().integrate(value)

            // Bit fields
            kclass == Boolean::class -> BooleanConverter().integrate(value)
            kclass == ByteArray::class -> ByteArrayConverter().integrate(value)
            kclass == Byte::class -> ByteConverter().integrate(value)

            // Numbers
            kclass == Int::class -> IntConverter().integrate(value)
            kclass == Long::class -> LongConverter().integrate(value)
            kclass == Short::class -> ShortConverter().integrate(value)
            kclass == Float::class -> FloatConverter().integrate(value)
            kclass == Double::class -> DoubleConverter().integrate(value)
            kclass == BigInteger::class -> BigIntConverter().integrate(value)
            kclass == BigDecimalConverter::class -> BigDecimalConverter().integrate(value)

            // Dates
            kclass == Date::class -> DateConverter().integrate(value)
            kclass == Calendar::class -> CalendarConverter().integrate(value)

            // Custom types
            kclass == JSONObject::class -> JsonConverter().integrate(value)

            // Enum
            kclass.java.isEnum -> EnumConverter(kclass as KClass<Enum<*>>).integrate(value) as T
            else -> UnknownTypeConverter().integrate(value)
        } as T
    }
}