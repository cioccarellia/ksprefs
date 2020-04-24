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
import com.cioccarellia.kspref.config.KspConfig
import com.cioccarellia.kspref.dispatcher.KspDispatcher
import com.cioccarellia.kspref.extensions.toKey
import com.cioccarellia.kspref.namespace.Namespace

class KsPrefs(
    context: Context,
    namespace: String = Namespace.default(context),
    config: KspConfig.() -> Unit = {}
) {
    @PublishedApi
    internal val dispatcher: KspDispatcher = KspDispatcher(context, namespace)

    companion object {
        lateinit var config: KspConfig
    }

    init {
        Companion.config = KspConfig().apply(config)
    }

    fun expose() = dispatcher.expose()

    inline fun <reified T> push(
        key: String,
        value: T
    ) = dispatcher.push(key, value)

    inline fun <reified T> pull(
        key: String,
        default: T
    ) = dispatcher.pull(key, default)

    fun delete(
        key: String
    ) = dispatcher.delete(key)
}