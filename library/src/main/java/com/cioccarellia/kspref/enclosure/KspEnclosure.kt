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
import android.content.Context
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.model.CommitStrategy
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.EnginePicker
import com.cioccarellia.kspref.engine.Transmission
import com.cioccarellia.kspref.extensions.*

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("CommitPrefEdits")
@PublishedApi
internal class KspEnclosure(
    private val namespace: String,
    context: Context,
    internal var sharedReader: Reader = context.getPrefs(namespace),
    internal var sharedWriter: Writer = sharedReader.edit(),
    private val engine: Engine = EnginePicker.select(context)
) {
    private inline fun deriveVal(
        value: ByteArray
    ): ByteArray = if (KsPrefs.config.engineIterations == 1) {
        engine.apply(
            Transmission(value)
        ).payload
    } else {
        var iterationsLeft = KsPrefs.config.engineIterations
        var bytes = value

        do {
            bytes = engine.apply(Transmission(bytes)).payload
            iterationsLeft--
        } while (iterationsLeft > 0)

        bytes
    }

    private inline fun integrateVal(
        value: ByteArray
    ): ByteArray = if (KsPrefs.config.engineIterations == 1) {
        engine.revert(
            Transmission(value)
        ).payload
    } else {
        var iterationsLeft = KsPrefs.config.engineIterations
        var bytes = value

        do {
            bytes = engine.revert(Transmission(bytes)).payload
            iterationsLeft--
        } while (iterationsLeft > 0)

        bytes
    }


    private inline fun deriveKey(
        value: String
    ): String = engine.apply(Transmission(value.bytes())).payload.string()

    private inline fun integrateKey(
        value: String
    ): String = engine.revert(Transmission(value.bytes())).payload.string()

    @PublishedApi
    internal fun read(
        key: String,
        default: ByteArray
    ) = integrateVal(
        sharedReader.read(
            deriveKey(key),
            deriveVal(default)
        )
    )

    @PublishedApi
    internal fun readUnsafe(
        key: String
    ) = integrateVal(
        sharedReader.readUnsafe(
            deriveKey(key)
        )
    )

    @PublishedApi
    internal fun write(
        key: String,
        value: ByteArray,
        strategy: CommitStrategy
    ) = with(sharedWriter) {
        write(
            deriveKey(key),
            deriveVal(value)
        )
        finalize(
            strategy
        )
    }

    @PublishedApi
    internal fun exists(
        key: String
    ) = sharedReader.exists(key)

    @PublishedApi
    internal fun save(
        commitStrategy: CommitStrategy
    ) {
        with(sharedWriter) {
            forceFinalization(
                commitStrategy = commitStrategy
            )
        }
    }

    @PublishedApi
    internal fun remove(
        key: String
    ) {
        sharedReader.edit()
            .delete(
                deriveKey(key)
            )
            .finalize(KsPrefs.config.commitStrategy)
    }
}