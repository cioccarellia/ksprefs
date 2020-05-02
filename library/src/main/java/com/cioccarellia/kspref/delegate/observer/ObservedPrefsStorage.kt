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

import android.content.SharedPreferences
import com.cioccarellia.kspref.KsPrefs
import kotlin.reflect.KClass

object ObservedPrefsStorage {
    private lateinit var actualListener: SharedPreferences.OnSharedPreferenceChangeListener
    private val observedPrefs: MutableMap<String, ObservedPref> = mutableMapOf()

    private fun <T : Any> listener(
        ref: DelegatePrefObserver<in T>
    ) = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        // We get the observer data by the tracked preference key,
        // and if the key is tracked we proceed
        val observedPref = observedPrefs[key] ?: return@OnSharedPreferenceChangeListener

        // We prefetch the old value
        val oldValue = ref.value as T

        // We fetch the latest SharedPrefs value
        val newValue = ref.prefs.dispatcher.pull(
            key, kclass = observedPref.valueType as KClass<T>
        )

        // We update the in-class value
        ref.value = newValue

        // We get the observer code which was created
        // and we cast it to a T-parameterized lambda
        val observer = observedPref.observer as (T, T) -> Unit

        // We invoke the actual code
        observer(oldValue, newValue)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> DelegatePrefObserver<T>.attachWith(kclass: KClass<out T>) {
        if (!ObservedPrefsStorage::actualListener.isInitialized) {
            prefs.expose().registerOnSharedPreferenceChangeListener(
                listener(
                    this
                ).also {
                    actualListener = it
                }
            )
        }

        observedPrefs[key] = ObservedPref(
            observer as (Any, Any) -> Unit,
            kclass,
            actualListener
        )
    }

    internal fun detach(
        prefs: KsPrefs
    ) {
        if (ObservedPrefsStorage::actualListener.isInitialized) {
            prefs.expose().unregisterOnSharedPreferenceChangeListener(actualListener)
            observedPrefs.clear()
        }
    }
}