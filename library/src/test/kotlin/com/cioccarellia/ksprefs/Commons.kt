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
package com.cioccarellia.ksprefs

import android.content.Context
import android.util.Base64
import androidx.test.core.app.ApplicationProvider
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.cioccarellia.ksprefs.config.model.KeySize
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class Commons {
    private val context: Context = ApplicationProvider.getApplicationContext()

    private val ksprefs = KsPrefs(context) {
        encryptionType = EncryptionType.PlainText()
        autoSave = AutoSavePolicy.AUTOMATIC
        commitStrategy = CommitStrategy.APPLY
    }


    @Test
    fun `Check clear() removal`() {
        assertThat(ksprefs.raw().isEmpty()).isTrue()

        ksprefs.push("test_key1", "A")
        ksprefs.push("test_key2", "B")
        ksprefs.push("test_key3", "C")
        ksprefs.clear()

        assertThat(ksprefs.raw().isEmpty()).isTrue()
    }
}