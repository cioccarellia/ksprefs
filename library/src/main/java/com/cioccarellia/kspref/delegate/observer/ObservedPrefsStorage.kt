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
import android.util.Log
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.defaults.Defaults
import kotlin.reflect.KClass

internal object ObservedPrefsStorage {
    private lateinit var actualListener: SharedPreferences.OnSharedPreferenceChangeListener
    private val observedPrefs: MutableMap<String, ObservedPref<*>> = mutableMapOf()

    // FIXME clean up this
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> listener() =
        SharedPreferences.OnSharedPreferenceChangeListener { _, keyDerivative ->
            if (observedPrefs.isEmpty()) return@OnSharedPreferenceChangeListener

            val _observedPref = observedPrefs.entries.take(1)[0].value

            // We decipher the given key
            val key = _observedPref.prop.prefs.engine.integrate(keyDerivative)

            // We get the observer data by the tracked preference key,
            // and if the key is tracked we proceed
            val observedPref = observedPrefs[key] ?: return@OnSharedPreferenceChangeListener

            val ref = observedPref.prop as DelegatePrefObserver<T>

            // We prefetch the old value
            val oldValue = ref.value as T

            // We fetch the latest SharedPrefs value
            val newValue = ref.prefs.dispatcher.pull(
                key, kclass = observedPref.valueType as KClass<T>
            )

            if (!KsPrefs.config.observeEqualUpdates && oldValue == newValue) return@OnSharedPreferenceChangeListener

            // We update the in-class value
            ref.value = newValue

            // We get the observer code which was created
            // and we cast it to a T-parameterized lambda
            val observer = observedPref.observer as (T, T) -> Unit

            // We invoke the actual code
            observer(oldValue, newValue)
        }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> DelegatePrefObserver<T>.attach(kclass: KClass<out T>) {
        if (!ObservedPrefsStorage::actualListener.isInitialized) {
            prefs.expose().registerOnSharedPreferenceChangeListener(
                listener<T>().also {
                    actualListener = it
                }
            )
        }

        if (observedPrefs[key] != null) {
            Log.w(
                Defaults.TAG,
                "A pref observer is already registered for '$key'. Disabling the previous one."
            )
        }

        observedPrefs[key] = ObservedPref(
            observer as (Any, Any) -> Unit,
            kclass,
            actualListener,
            this
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