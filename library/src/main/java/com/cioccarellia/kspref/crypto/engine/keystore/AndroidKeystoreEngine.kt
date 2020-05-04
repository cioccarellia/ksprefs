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
package com.cioccarellia.kspref.crypto.engine.keystore

import android.content.Context
import com.cioccarellia.kspref.crypto.CryptoEngine
import com.cioccarellia.kspref.crypto.Engine
import com.cioccarellia.kspref.crypto.Transmission
import com.cioccarellia.kspref.crypto.secondary.SecondaryEnginePicker
import java.security.KeyStore

class AndroidKeystoreEngine(
    private val context: Context,
    private val alias: String,
    private val keyTagLengthInBits: Int
) : Engine(), CryptoEngine {

    private val KEYSTORE_TYPE = "AndroidKeyStore"

    private val keyStore: KeyStore
        get() = KeyStore.getInstance(KEYSTORE_TYPE).also {
            it.load(null)
        }

    private val subEngine
        get() = SecondaryEnginePicker.select(alias, context, keyStore)

    override fun apply(incoming: Transmission) = Transmission(
        encrypt(incoming.payload)
    )

    override fun revert(outgoing: Transmission) = Transmission(
        decrypt(outgoing.payload)
    )

    override fun encrypt(input: ByteArray) = subEngine.encrypt(input)

    override fun decrypt(cipherText: ByteArray) = subEngine.decrypt(cipherText)
}