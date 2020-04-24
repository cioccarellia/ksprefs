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

import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.EnginePicker
import com.cioccarellia.kspref.engine.Transmission
import com.cioccarellia.kspref.extensions.*

@PublishedApi
internal class KspEnclosure(
    private val namespace: String,
    internal val handle: Reader,
    private val engine: Engine = EnginePicker.select()
) {
    private inline fun engineApply(
        value: ByteArray
    ): ByteArray = engine.apply(Transmission(value)).payload

    private inline fun engineRevert(
        value: ByteArray
    ): ByteArray = engine.revert(Transmission(value)).payload

    @PublishedApi
    internal fun read(
        key: String,
        default: ByteArray
    ) = engineRevert(handle.read(key, engineApply(default)))

    @PublishedApi
    internal fun write(
        key: String,
        value: ByteArray
    ) = handle.edit()
        .write(key, engineApply(value))
        .finalize()

    internal fun save(
        commitStrategy: CommitStrategy
    ) = handle.edit().forceFinalization(
        commitStrategy = commitStrategy
    )

    internal fun delete(
        key: String
    ) = handle.edit()
        .delete(key)
        .finalize()
}