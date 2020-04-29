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
package com.cioccarellia.kspref.engine

import android.util.Base64
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.crypto.ByteTransformationStrategy
import com.cioccarellia.kspref.engine.model.AesEngine
import com.cioccarellia.kspref.engine.model.Base64Engine
import com.cioccarellia.kspref.engine.model.PlainTextEngine
import com.cioccarellia.kspref.exception.KsPrefEngineException
import com.cioccarellia.kspref.extensions.byteArray

object EnginePicker {
    fun select(): Engine = when (KsPrefs.config.encryption.transformation) {
        ByteTransformationStrategy.PLAIN_TEXT -> PlainTextEngine()
        ByteTransformationStrategy.BASE64 -> Base64Engine(
            Base64.DEFAULT
        )
        ByteTransformationStrategy.AES -> try {
            AesEngine(
                KsPrefs.config.encryption.key!!.byteArray(),
                KsPrefs.config.encryption.keySize
            )
        } catch (knpe: KotlinNullPointerException) {
            throw KsPrefEngineException("KsPref encryptionConfig key is not initialized", knpe)
        }
    }
}