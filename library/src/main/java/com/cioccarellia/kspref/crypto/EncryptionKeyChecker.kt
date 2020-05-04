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
package com.cioccarellia.kspref.crypto

import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.model.KeySizeCheck
import com.cioccarellia.kspref.config.model.KeySizeMismatchFallbackStrategy
import com.cioccarellia.kspref.exception.KsPrefKeySizeMismatchException

object EncryptionKeyChecker {
    fun approve(
        key: SymmetricKey,
        keyCheckSize: KeySizeCheck
    ): SymmetricKey = if (!key.matches(keyCheckSize)) {
        when (KsPrefs.config.keySizeMismatch) {
            KeySizeMismatchFallbackStrategy.TRIM -> key.trim(keyCheckSize)
            KeySizeMismatchFallbackStrategy.CRASH -> throw KsPrefKeySizeMismatchException(
                expected = keyCheckSize.bitCount(),
                actual = key.bitCount()
            )
            KeySizeMismatchFallbackStrategy.NOTHING -> key
        }
    } else key
}