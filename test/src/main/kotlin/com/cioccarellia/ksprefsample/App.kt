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
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.KspConfig
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.KeySize
import com.cioccarellia.ksprefsample.prefcenter.StartCounterPrefCenter

class App : Application() {

    /**
     * DO NOT use this code inside a production application.
     * This is shaped so that the library sample app is able
     * to access ksprefs internal configuration, which is
     * by default hidden.
     * Follow the readme guidelines at ksprefs'github page
     * to get started.
     * */
    companion object {
        lateinit var appContext: Context

        private val aes = EncryptionType.AesEcb("dadaaaaaaaaaaaaa", KeySize.SIZE_128_BITS)
        private val keyStore = EncryptionType.KeyStore("alias0")

        internal val globalConfigStateFx: KspConfig.() -> Unit = {
            encryptionType = aes
            autoSave = AutoSavePolicy.AUTOMATIC
        }

        val prefs by lazy {
            KsPrefs(appContext, config = globalConfigStateFx)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this

        StartCounterPrefCenter.increment()
    }
}