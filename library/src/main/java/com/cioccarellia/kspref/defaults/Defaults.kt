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
package com.cioccarellia.kspref.defaults

import android.content.Context
import android.util.Base64
import com.cioccarellia.kspref.config.AutoSavePolicy
import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.config.crypto.parameters.KeyTagSize
import com.cioccarellia.kspref.config.crypto.parameters.KeyTrimmingOption
import java.util.*

internal object Defaults {
    /** Library glabal */
    const val TAG = "KsPref"

    /** SharedPreferences */
    const val MODE = Context.MODE_PRIVATE

    /** Encoding */
    val CHARSET = Charsets.UTF_8

    /** Saving */
    val AUTO_SAVE_POLICY = AutoSavePolicy.AUTO
    val COMMIT_STRATEGY = CommitStrategy.ASYNC_APPLY

    /** Encryption */
    const val DEFAULT_BASE64_FLAGS = Base64.NO_CLOSE or Base64.NO_WRAP
    val KEY_SIZE_TRIM_OPTION = KeyTrimmingOption.TRIM_128
    val KEY_TAG_SIZE = KeyTagSize.SIZE_128
    val DEFAULT_RSA_KEY_DURATION: Pair<Date, Date> by lazy {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance().apply {
            add(Calendar.YEAR, 10)
        }

        start.time to end.time
    }
}