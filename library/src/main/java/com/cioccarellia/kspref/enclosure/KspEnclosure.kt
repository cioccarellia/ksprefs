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
@file:Suppress("NOTHING_TO_INLINE")

package com.cioccarellia.kspref.enclosure

import android.content.Context
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.EnginePicker
import com.cioccarellia.kspref.engine.Transmission
import com.cioccarellia.kspref.extensions.*
import com.cioccarellia.kspref.extensions.getPrefs

@PublishedApi
internal class KspEnclosure(
    context: Context,
    private val namespace: String,
    private val engine: Engine = EnginePicker.select()
) {
    internal val handle by lazy(LazyThreadSafetyMode.NONE) { context.getPrefs(namespace) }

    private inline fun computeAndApply(
        value: ByteArray
    ): ByteArray = engine.apply(Transmission(value)).payload

    private inline fun computeAndRevert(
        value: ByteArray
    ): ByteArray = engine.revert(Transmission(value)).payload

    @PublishedApi
    internal fun read(
        key: String,
        default: ByteArray
    ) = computeAndRevert(
        handle.read(key, computeAndApply(default))
    )

    @PublishedApi
    internal fun write(
        key: String,
        value: ByteArray
    ) = handle.edit()
        .write(key, computeAndApply(value))
        .finalize()

    internal fun delete(
        key: String
    ) = handle.edit()
        .delete(key)
        .finalize()
}