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
package com.cioccarellia.ksprefs.config

import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.cioccarellia.ksprefs.config.model.KeySizeMismatchFallbackStrategy
import com.cioccarellia.ksprefs.defaults.Defaults
import java.nio.charset.Charset

/**
 * Global KsPrefs config object
 * */
data class KspConfig(
    @IntRange(from = 0x0000, to = 0x0010)
    var mode: Int = Defaults.MODE,
    var charset: Charset = Defaults.CHARSET,
    var xmlPrefix: String = Defaults.XML_PREFIX,

    var autoSave: AutoSavePolicy = Defaults.AUTO_SAVE_POLICY,
    var commitStrategy: CommitStrategy = Defaults.COMMIT_STRATEGY,

    var keyRegex: Regex? = null,
    var encryptionType: EncryptionType = EncryptionType.PlainText(),
    var keySizeMismatch: KeySizeMismatchFallbackStrategy = Defaults.KEY_SIZE_MISMATCH_STRATEGY
)