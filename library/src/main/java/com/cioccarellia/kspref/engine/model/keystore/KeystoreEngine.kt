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
package com.cioccarellia.kspref.engine.model.keystore

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import com.cioccarellia.kspref.engine.CryptoEngine
import com.cioccarellia.kspref.engine.Engine
import com.cioccarellia.kspref.engine.Transmission
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class KeystoreEngine(
    private val alias: String,
    private val base64Flags: Int
) : Engine(), CryptoEngine {

    override val algorithm = "AES/GCM/NoPadding"
    override val transformation = "AndroidKeyStore"

    private val keyStore = KeyStore.getInstance(transformation)

    private lateinit var iv: ByteArray

    init {
        keyStore.load(null)
    }

    override fun apply(incoming: Transmission) = Transmission(
        Base64.encode(encrypt(incoming.payload), base64Flags)
    )

    override fun revert(outgoing: Transmission) = Transmission(
        decrypt(
            Base64.decode(outgoing.payload, base64Flags)
        )
    )

    private fun encryptionKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, transformation)

        val keyProps = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        keyGenerator.init(
            KeyGenParameterSpec.Builder(alias, keyProps).run {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                build()
            }
        )

        return keyGenerator.generateKey()
    }

    override fun encrypt(input: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey())
        iv = cipher.iv
        return cipher.doFinal(input)
    }

    private fun decryptionKey(): SecretKey {
        return (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }

    override fun decrypt(cipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, decryptionKey(), spec)
        return cipher.doFinal(cipherText)
    }
}