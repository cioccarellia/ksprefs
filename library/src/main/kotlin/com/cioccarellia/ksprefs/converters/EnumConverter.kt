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

import com.cioccarellia.ksprefs.extensions.string
import kotlin.reflect.KClass

@PublishedApi
internal open class EnumConverter(
    private val kclass: KClass<Enum<*>>
) : TypeConverter<Enum<*>>() {

    override fun derive(value: Enum<*>) = kclass.java.enumConstants
        ?.map { it.toString() }
        ?.indexOf(value.toString())
        .toString()
        .toByteArray()

    override fun integrate(value: ByteArray): Enum<*> {
        val enumIndex = value.string().toInt()
        return kclass.java.enumConstants!![enumIndex]
    }
}