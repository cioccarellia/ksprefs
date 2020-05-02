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
import com.cioccarellia.kspref.config.AutoSavePolicy
import com.cioccarellia.kspref.config.CommitStrategy
import com.cioccarellia.kspref.config.crypto.BlockCipherEncryptionMode
import com.cioccarellia.kspref.config.crypto.ByteTransformationStrategy
import com.cioccarellia.kspref.config.crypto.KeySizeTrimmingOption

object Defaults {
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
    val TRANSFORMATION = ByteTransformationStrategy.PLAIN_TEXT
    val BLOCK_CIPHER_ENCRYPTION_MODE = BlockCipherEncryptionMode.ECB
    val KEY_SIZE_TRIM_OPTION = KeySizeTrimmingOption.TRIM_128
}