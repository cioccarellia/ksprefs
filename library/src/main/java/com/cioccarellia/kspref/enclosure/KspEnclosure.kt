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

import android.annotation.SuppressLint
import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.EnginePicker
import com.cioccarellia.kspref.engine.Transmission
import com.cioccarellia.kspref.extensions.*

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("CommitPrefEdits")
@PublishedApi
internal class KspEnclosure(
    private val namespace: String,
    internal var sharedReader: Reader,
    internal var sharedWriter: Writer = sharedReader.edit(),
    private val engine: Engine = EnginePicker.select()
) {
    private inline fun engineApply(
        value: ByteArray
    ): ByteArray = engine.apply(Transmission(value)).payload

    private inline fun engineApply(
        value: String
    ): String = engine.apply(Transmission(value.byteArray())).payload.string()

    private inline fun engineRevert(
        value: ByteArray
    ): ByteArray = engine.revert(Transmission(value)).payload

    private inline fun engineRevert(
        value: String
    ): String = engine.revert(Transmission(value.byteArray())).payload.string()

    @PublishedApi
    internal fun read(
        key: String,
        default: ByteArray
    ) = engineRevert(
        sharedReader.read(
            engineApply(key),
            engineApply(default)
        )
    )

    @PublishedApi
    internal fun write(
        key: String,
        value: ByteArray
    ) = with(sharedWriter) {
        write(
            engineApply(key),
            engineApply(value)
        )
        finalize()
    }

    internal fun save(
        commitStrategy: CommitStrategy
    ) = with(sharedWriter) {
        forceFinalization(
            commitStrategy = commitStrategy
        )
    }

    internal fun remove(
        key: String
    ) = sharedReader.edit()
        .delete(
            engineApply(key)
        )
        .finalize()
}