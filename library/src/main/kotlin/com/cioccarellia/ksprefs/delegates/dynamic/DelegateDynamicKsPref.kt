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
package com.cioccarellia.ksprefs.delegates.dynamic

import com.cioccarellia.ksprefs.KsPrefs
import kotlin.reflect.KProperty

/**
 * Provides a dynamic delegate property getter and setter.
 * */
class DelegateDynamicKsPref<T : Any>(
    private val prefs: KsPrefs,
    private val key: String,
    private val value: T
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return prefs.pull(key, value)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return prefs.push(key, value)
    }
}