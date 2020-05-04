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
package com.cioccarellia.kspref.extensions

import android.content.SharedPreferences
import androidx.annotation.CheckResult
import com.cioccarellia.kspref.exception.NoSuchPrefKeyException

typealias Reader = SharedPreferences

@CheckResult
internal fun Reader.read(
    key: String,
    default: ByteArray
): ByteArray = getString(key, default.string())?.bytes() ?: "".bytes()

@CheckResult
internal fun Reader.readUnsafe(
    key: String
): ByteArray = try {
    getString(key, null)!!.bytes()
} catch (knpe: KotlinNullPointerException) {
    throw NoSuchPrefKeyException(key)
}

@CheckResult
internal fun Reader.exists(
    key: String
): Boolean = getString(key, null) != null