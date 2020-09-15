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

import com.cioccarellia.ksprefs.extensions.bytes
import com.cioccarellia.ksprefs.extensions.string

@PublishedApi
internal class FloatConverter : TypeConverter<Float>() {
    override fun derive(value: Float) = value.toString().bytes()
    override fun integrate(value: ByteArray) = value.string().toFloat()
}
