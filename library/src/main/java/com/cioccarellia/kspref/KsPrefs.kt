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
package com.cioccarellia.kspref

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.CheckResult
import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.config.KspConfig
import com.cioccarellia.kspref.delegate.observer.ObservedPrefsStorage
import com.cioccarellia.kspref.dispatcher.KspDispatcher
import com.cioccarellia.kspref.namespace.Namespace

class KsPrefs(
    appContext: Context,
    namespace: String = Namespace.default(appContext),
    config: KspConfig.() -> Unit = {}
) {
    companion object {
        /**
         * Config object used to retrieve behaviour and strategies
         * */
        internal val config: KspConfig by lazy { KspConfig() }
    }

    init {
        Companion.config.run(config)
    }

    @PublishedApi
    internal val dispatcher = KspDispatcher(namespace, appContext)

    /**
     * Exposes the internal [Shared Preferences][SharedPreferences]
     * object, used to perform
     *
     * @return A reference to the internal [Shared Preferences][SharedPreferences] object
     * */
    fun expose(): SharedPreferences = dispatcher.expose()

    /**
     * Puts a value into the [Shared Preferences][SharedPreferences] storage.
     *
     * @param key       The key of the target field
     * @param value     The value to be converted and stored
     * */
    fun <T : Any> push(
        key: String,
        value: T
    ) {
        dispatcher.push(key, value)
    }

    /**
     * Gets a value from the [Shared Preferences][SharedPreferences] storage.
     *
     * @param key       The key of the target field
     * @param default   The default value, in case the key matches nothing
     *
     * @return          The value KsPref got back for the matching key, or [default]
     * */
    @CheckResult
    fun <T : Any> pull(
        key: String,
        default: T
    ) = dispatcher.pull(key, default)

    /**
     * Unsafely gets a value from the [Shared Preferences][SharedPreferences] storage.
     *
     * @throws NoSuchPrefKeyException if the key finds nothing
     *
     * @param key The key of the target field
     *
     * @return The value KsPref got back for the matching key, or an exception
     * */
    @CheckResult
    inline fun <reified T : Any> pull(
        key: String
    ) = dispatcher.pull(key, T::class)

    /**
     * Forces a commit() or an apply() to happen
     * */
    fun save(
        commitStrategy: CommitStrategy = config.commitStrategy
    ) = dispatcher.save(commitStrategy)

    /**
     * Removes a preference from the storage
     * */
    fun remove(
        key: String
    ) = dispatcher.remove(key)

    /**
     * KsPrefs will register a SharedPreferences listener
     * if you use observers within your codebase.
     * It is always a good practise to clear those up
     * when your app terminates, to avoid creating memory leaks.
     * */
    fun destroy() {
        ObservedPrefsStorage.detach(this)
    }
}