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
package com.cioccarellia.ksprefs.extensions

import android.content.SharedPreferences
import androidx.annotation.CheckResult
import com.cioccarellia.ksprefs.exceptions.NoSuchKeyException

/**
 *
 * */
@CheckResult
internal fun SharedPreferences.read(
    key: String,
    default: ByteArray,
    null_dfu: ByteArray = "".bytes()
): ByteArray = getString(key, default.string())?.bytes() ?: null_dfu


/**
 * Faster than `read`: we don't compute the derivative of the default parameter
 * unless we have to (null value from shared preferences)
 * */
@CheckResult
internal inline fun SharedPreferences.read_efficient(
    key: String,
    default: () -> ByteArray,
): ByteArray = getString(key, null)?.bytes() ?: default()


@CheckResult
internal fun SharedPreferences.readOrThrow(
    key: String
): ByteArray = try {
    getString(key, null)!!.bytes()
} catch (knpe: KotlinNullPointerException) {
    throw NoSuchKeyException(key)
}

@CheckResult
internal fun SharedPreferences.exists(
    key: String
): Boolean = getString(key, null) != null