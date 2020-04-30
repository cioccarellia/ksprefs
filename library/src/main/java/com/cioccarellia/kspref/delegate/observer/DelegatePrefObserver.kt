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
package com.cioccarellia.kspref.delegate.observer

import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.delegate.observer.ObservedPrefsStorage.attachWith
import kotlin.reflect.KProperty

class DelegatePrefObserver<T : Any>(
    internal val prefs: KsPrefs,
    internal val key: String,
    internal var value: T,
    internal val observer: (T, T) -> Unit
) {
    init {
        attachWith(value::class)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = newValue
        prefs.push(key, newValue)
    }
}