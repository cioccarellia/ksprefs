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
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
class AESTests {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val ksprefs = KsPrefs(context) {
        encryptionType = EncryptionType.AesEcb(
            "QFLWyiXg5iSpZKBQjxncvoK16oDQRoC0",
            keySize = KeySize.SIZE_256_BITS,
            Base64.URL_SAFE
        )
        autoSave = AutoSavePolicy.AUTOMATIC
        commitStrategy = CommitStrategy.APPLY
    }

    @Test
    fun `AES-Backed push() and pull() behaviour`() {
        val key = "x"
        val value = "y"

        ksprefs.push(key, value)
        assertThat(ksprefs.pull<String>(key)).isEqualTo(value)
    }

    @Test
    fun `AES-Backed Existence of SP entry`() {
        val key = "x"
        val value = "y"

        ksprefs.push(key, value)
        assertThat(ksprefs.exists(key)).isTrue()
    }


    @Test
    fun `AES-Backed removal of SP entry`() {
        val key = "x"
        val value = "y"

        ksprefs.push(key, value)
        assertThat(ksprefs.exists(key)).isTrue()

        ksprefs.remove(key)
        assertThat(ksprefs.exists(key)).isFalse()
    }


    @Test
    fun `AES-Backed integrity with raw()`() {
        val key = "x"
        val values = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())

        values.forEachIndexed { i, it ->
            ksprefs.push("${key}_$i", it)
        }


        ksprefs.raw().forEach { (_, value) ->
            assertThat(value in values)
        }
    }

    @Test
    fun `AES non-ascii value`() {
        val key = "x"
        val value = "\uD83C\uDF89"

        ksprefs.push(key, value)
        assertThat(ksprefs.pull<String>(key)).isEqualTo(value)
    }
}