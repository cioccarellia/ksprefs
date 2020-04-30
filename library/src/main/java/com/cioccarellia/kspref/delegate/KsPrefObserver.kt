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
package com.cioccarellia.kspref.delegate

import android.content.SharedPreferences
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.delegate.ObservedPrefs.setListener
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object ObservedPrefs {
    private val observerList: MutableMap<String, Pair<(Any) -> Unit, KClass<*>>> = mutableMapOf()

    var isRefSet = true

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> KsPrefObserver<T>.setListener(kClass: KClass<out T>) {
        observerList[this.key] = Pair(this.observer as (Any) -> Unit, kClass)

        if (isRefSet) {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                val observer = observerList[key]!! as Pair<(T) -> Unit, KClass<*>>
                val fetchedValue = this.prefs.dispatcher.pull(key, observer.second as KClass<T>)
                this.value = fetchedValue
                observer.first(fetchedValue)
            }


            prefs.expose().registerOnSharedPreferenceChangeListener(listener)
            isRefSet = false
        }

        return
    }
}

class KsPrefObserver<T : Any>(
    internal val prefs: KsPrefs,
    internal var value: T,
    internal val key: String,
    internal val observer: (T) -> Unit
) {
    init {
        setListener(value::class)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = newValue
        prefs.push(key, newValue)
    }
}