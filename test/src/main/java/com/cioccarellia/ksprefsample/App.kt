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
package com.cioccarellia.ksprefsample

import android.app.Application
import android.content.Context
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.crypto.EncryptionType
import com.cioccarellia.kspref.config.model.KeySizeCheck
import com.cioccarellia.kspref.randomIV

class App : Application() {

    companion object {
        lateinit var appContext: Context

        val prefs by lazy {
            KsPrefs(appContext) {
                encryptionType = EncryptionType.AesCbc(
                    "aaaaaaaaaaaaaaaa",
                    keySize = KeySizeCheck.TRIM_128,
                    iv = KsPrefs.randomIV(16)
                )
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
        prefs.destroy()
    }
}