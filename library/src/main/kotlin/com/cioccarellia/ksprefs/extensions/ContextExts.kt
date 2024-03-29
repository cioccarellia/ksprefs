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

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.cioccarellia.ksprefs.KsPrefs

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun Context.sharedprefs(
    namespace: String,
    prefix: String = KsPrefs.config.xmlPrefix
): SharedPreferences = try {
    val actualStorageFileName = "${prefix}$namespace"
    getSharedPreferences(actualStorageFileName, KsPrefs.config.mode)!!
} catch (knpe: KotlinNullPointerException) {
    throw IllegalStateException("Could not create SharedPreferences instance")
}