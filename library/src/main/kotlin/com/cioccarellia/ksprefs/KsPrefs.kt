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
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.cioccarellia.ksprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.KspConfig
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.cioccarellia.ksprefs.dispatcher.KspDispatcher
import com.cioccarellia.ksprefs.engines.base.Engine
import com.cioccarellia.ksprefs.exceptions.NoSuchKeyException
import com.cioccarellia.ksprefs.namespace.Namespace
import kotlin.reflect.KClass

class KsPrefs(
    appContext: Context,
    val namespace: String = Namespace.default(appContext),
    config: KspConfig.() -> Unit = {}
) {

    companion object {
        /**
         * Library-wide configuration object used
         * to retrieve strategies and policies, to allow
         * different modules of the library to maintain a
         * consistent behaviour.
         *
         * It is kept internal to avoid changing settings at
         * run-time, which may interfere with KsPrefs's logic.
         * */
        internal val config: KspConfig by lazy { KspConfig() }
    }

    init {
        // Loads up the passed configuration
        Companion.config.run(config)
    }

    @PublishedApi
    internal val dispatcher = KspDispatcher(namespace, appContext)

    private var lifecycle: Lifecycle? = null

    internal val engine: Engine
        get() = dispatcher.engine()

    /**
     * Exposes the internal [Shared Preferences][SharedPreferences]
     * object, used to perform direct I/O operations on the underlying
     * XML file.
     *
     * Warning: if you are using an [encryption type][EncryptionType]
     * different than [PlainText][EncryptionType.PlainText] you will be
     * reading scrambled data, as the SharedPreferences android counterpart
     * is not aware of our encryption layer, as the conversion will be executed
     * without an [engine][Engine].
     *
     * Plus, using this object to read/write isn't really the point of
     * using a wrapper library for SharedPreferences, so it is discouraged,
     * unless you know what you are doing.
     *
     *
     * @return A reference to the internal [Shared Preferences][SharedPreferences] object.
     * */
    fun getSharedPreferences(): SharedPreferences = dispatcher.expose()

    /**
     * This function pushes a value in the [Shared Preferences][SharedPreferences] object,
     * and eventually saves it to the actual preference file.
     *
     * The [value] is first converted into its proper ByteArray representation.
     * The [key] is just converted from String to ByteArray.
     * [commitStrategy] is used to define the commit behaviour, and it can be parameterized.
     * Then, both [key] and [value] are passed through the picked [engine][Engine].
     * Both [key] and [value] are derived once, becoming key' and value'.
     * The new value is pushed into SharedPreferences and committed, if and according to
     * the [commitStrategy] and the [auto save policy][AutoSavePolicy].
     *
     *
     * @param[key] The key for the target field.
     * @param[value] The value to be derived and stored.
     * @param[commitStrategy] The strategy defining how to finalize this operation.
     * */
    @Synchronized
    fun <T : Any> push(
        key: String,
        value: T,
        commitStrategy: CommitStrategy = config.commitStrategy
    ): Unit = dispatcher.push(
        key, value, commitStrategy
    )

    /**
     * This function pushes a value in the [Shared Preferences][SharedPreferences] object,
     * without writing it out to the preference file.
     *
     * The [value] is first converted into its proper ByteArray representation.
     * The [key] is just converted from String to ByteArray
     * Then, both [key] and [value] are passed through the picked [engine][Engine].
     * Both [key] and [value] are derived once, becoming key' and value'.
     * The new value is pushed into SharedPreferences, but it isn't committed.
     * This saves up a lot of time for batch operations, since the actual saving to the
     * preferences XML file can be forced with [save]
     *
     *
     * @param[key] The key for the target field.
     * @param[value] The value to be derived and stored.
     * */
    @Synchronized
    fun <T : Any> queue(
        key: String,
        value: T
    ): Unit = dispatcher.push(key, value, strategy = CommitStrategy.NONE)

    /**
     * This function pulls a value from the [Shared Preferences][SharedPreferences] object.
     *
     * The [fallback] value is converted into its proper ByteArray representation.
     * The [key] is just converted from String to ByteArray
     * Then, both [key] and [fallback] are passed through the picked [engine][Engine].
     * Both [key] and [fallback] are derived once, becoming key' and fallback'.
     * key' and fallback' are used to pull up the value which may be found inside the shared preferences file.
     * If the result gives back a non-empty ByteArray, it is chosen as value'. Otherwise, fallback' is used.
     * Both key' and value' are integrated once, converted, and then returned.
     *
     *
     * @param[key] The key for the target field.
     * @param[fallback] The fallback value, in case the given key matches nothing.
     *
     * @return The value KsPref got back for the matching key, or [fallback].
     * */
    @CheckResult
    @Synchronized
    fun <T : Any> pull(
        key: String,
        fallback: T
    ): T = dispatcher.pull(key, fallback)

    /**
     * This function (unsafely) pulls a value from the [Shared Preferences][SharedPreferences] object.
     *
     * The [key] is converted from String to ByteArray
     * Then, [key] is passed through the picked [engine][Engine].
     * [key] is derived once, becoming key', and it's used to pull up
     * the value which may be found inside the shared preferences file.
     * If the result gives back a non-empty ByteArray, it is chosen as value'.
     * Otherwise, [NoSuchKeyException] is thrown.
     * Both key' and value' are integrated once, converted, and then returned.
     *
     * This function must be inlined, and can not be used from java.
     *
     * This function is unsafe because if the key isn't found, an exception is
     * raised, to enforce its never null return type.
     *
     *
     * @param[T] The type value' will be converted to.
     * @param[key] The key for the target field.
     *
     * @return The value KsPref got back for the matching key.
     * @throws NoSuchKeyException If no value is found for the given [key].
     * */
    @CheckResult
    inline fun <reified T : Any> pull(
        key: String
    ): T {
        synchronized(this) {
            return dispatcher.pull(key, kclass = T::class)
        }
    }

    /**
     * This function (unsafely) pulls a value from the [Shared Preferences][SharedPreferences] object.
     *
     * The [key] is converted from String to ByteArray
     * Then, [key] is passed through the picked [engine][Engine].
     * [key] is derived once, becoming key', and it's used to pull up
     * the value which may be found inside the shared preferences file.
     * If the result gives back a non-empty ByteArray, it is chosen as value'.
     * Otherwise, [NoSuchKeyException] is thrown.
     * Both key' and value' are integrated once, converted, and then returned.
     *
     * This function is its inline counterpart [pull] for non type-reifiable contexts.
     *
     * This function is unsafe because if the key isn't found, an exception is
     * raised, to enforce its never null return type.
     *
     *
     * @param[key] The key for the target field.
     * @param[kclass] The type value' will be converted to.
     *
     * @return The value KsPref got back for the matching key.
     * @throws NoSuchKeyException If no value is found for the given [key].
     * */
    @CheckResult
    @Synchronized
    fun <T : Any> pull(
        key: String,
        kclass: KClass<T>
    ): T = dispatcher.pull(key, kclass = kclass)

    /**
     * This function (unsafely) pulls a value from the [Shared Preferences][SharedPreferences] object.
     *
     * The [key] is converted from String to ByteArray
     * Then, [key] is passed through the picked [engine][Engine].
     * [key] is derived once, becoming key', and it's used to pull up
     * the value which may be found inside the shared preferences file.
     * If the result gives back a non-empty ByteArray, it is chosen as value'.
     * Otherwise, [NoSuchKeyException] is thrown.
     * Both key' and value' are integrated once, converted, and then returned.
     *
     * This function is its inline counterpart [pull] for non type-reifiable contexts.
     *
     * This function is unsafe because if the key isn't found, an exception is
     * raised, to enforce its never null return type.
     *
     *
     * @param[key] The key for the target field.
     * @param[jclass] The type value' will be converted to.
     *
     * @return The value KsPref got back for the matching key.
     * @throws NoSuchKeyException If no value is found for the given [key].
     * */
    @CheckResult
    @Synchronized
    fun <T : Any> pull(
        key: String,
        jclass: Class<T>
    ): T = dispatcher.pull(key, jclass.kotlin)


    /**
     * Returns a map containing every key-value pair stored by SharedPreferences.
     * */
    @CheckResult
    fun raw(): Map<String, *> = dispatcher.raw()

    /**
     * Checks whether a value exists under a given [key].
     *
     * The [key] is converted from String to ByteArray, and derived once.
     * key' is used to pull up the value from shared preferences.
     * The value found for the given [key] is analyzed.
     * If it exists, true is returned.
     *
     *
     * @param[key] The key for the target field.
     *
     * @return Whether the value exists inside the storage or not.
     * */
    @CheckResult
    @Synchronized
    fun exists(
        key: String
    ): Boolean = dispatcher.exists(key)

    /**
     * Forces [Shared Preferences][SharedPreferences] to save the value
     * to its XML file.
     *
     * As this is a prioritized operation, this function ignores the current
     * [auto save policy][AutoSavePolicy], while still enforcing the
     * selected [commit strategy][CommitStrategy].
     *
     * However, a [commitStrategy] can be specified, and it will have precedence,
     * overriding the global [strategy][KspConfig.commitStrategy] for this one operation.
     *
     *
     * @param[commitStrategy] Optional parameterization for the [commit strategy][KspConfig.commitStrategy].
     * */
    @Synchronized
    fun save(
        commitStrategy: CommitStrategy = config.commitStrategy
    ): Unit = dispatcher.save(commitStrategy)

    /**
     * Removes the entry matching the given [key] from [Shared Preferences][SharedPreferences].
     *
     * The [key] is converted from String to ByteArray, and derived once.
     * key' is used to natively remove the record from shared preferences.
     *
     *
     * @param[key] The key for the target field.
     * */
    @Synchronized
    fun remove(
        key: String
    ) {
        dispatcher.remove(key)
    }

    /**
     * Calls the [clear()] method on [Shared Preferences][SharedPreferences], and removes all entries.
     * */
    @Synchronized
    fun clear() {
        dispatcher.clear()
    }
}