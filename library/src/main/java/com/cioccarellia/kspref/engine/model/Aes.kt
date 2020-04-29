/**
 * Designed and developed by Andrea Cioccarelli (@cioccarellia)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cioccarellia.kspref.engine.model

import android.util.Base64
import com.cioccarellia.kspref.extensions.byteArray
import com.cioccarellia.kspref.extensions.emptyByteArray
import com.cioccarellia.kspref.extensions.string
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Aes(
    private var actualKey: String
) {
    private var secretKey: SecretKeySpec? = null
    private var tempKey: ByteArray = emptyByteArray()

    fun setKey(myKey: String) {
        val sha: MessageDigest

        try {
            tempKey = myKey.byteArray()
            sha = MessageDigest.getInstance("SHA-1")
            tempKey = sha.digest(tempKey)
            tempKey = tempKey.copyOf(16)
            secretKey = SecretKeySpec(tempKey, "AES")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun encrypt(strToEncrypt: String): String? {
        try {
            setKey(actualKey)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val bytes = cipher.doFinal(strToEncrypt.byteArray())
            return Base64.encode(bytes, Base64.DEFAULT).string()
        } catch (e: Exception) {
            println("Error while encrypting: $e")
        }
        return null
    }

    fun decrypt(strToDecrypt: String?): String? {
        try {
            setKey(actualKey)
            val cipher =
                Cipher.getInstance("AES/ECB/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            return String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        } catch (e: Exception) {
            println("Error while decrypting: $e")
        }
        return null
    }
}