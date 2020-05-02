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
package com.cioccarellia.kspref.exception

import android.util.Log
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.defaults.Defaults
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

internal class KsPrefEngineException(
    message: String = "",
    throwable: Throwable? = null
) : RuntimeException(message.trim(), throwable) {

    init {
        Log.e(Defaults.TAG, message)
    }

    companion object {
        fun convertFrom(
            throwable: Throwable?,
            operation: String
        ): KsPrefEngineException {
            val charset = KsPrefs.config.charset.name()

            return when (throwable) {
                is NoSuchAlgorithmException -> KsPrefEngineException(
                    "No algorithm found. $operation",
                    throwable
                )
                is NoSuchPaddingException -> KsPrefEngineException(
                    "Padding mechanism required but not encountered in the system. $operation",
                    throwable
                )
                is InvalidKeySpecException -> KsPrefEngineException(
                    "Invalid key specification. $operation",
                    throwable
                )
                is InvalidKeyException -> KsPrefEngineException(
                    "Invalid key supplied. $operation",
                    throwable
                )
                is IllegalArgumentException -> KsPrefEngineException(
                    "Key is empty. $operation",
                    throwable
                )
                is InvalidParameterSpecException -> KsPrefEngineException(
                    "Key is empty. $operation",
                    throwable
                )
                is IllegalBlockSizeException -> KsPrefEngineException(
                    "Illegal block size. $operation",
                    throwable
                )
                is BadPaddingException -> KsPrefEngineException("Bad padding. $operation", throwable)
                is UnsupportedEncodingException -> KsPrefEngineException(
                    "The charset encoding is unsupported ($charset). $operation",
                    throwable
                )
                is UnrecoverableEntryException -> KsPrefEngineException(
                    "The keystore entry cannot be recovered.",
                    throwable
                )
                is KeyStoreException -> KsPrefEngineException(
                    "Generic Android KeyStore exception.",
                    throwable
                )
                is NoSuchProviderException -> KsPrefEngineException(
                    "The required security provided cannot be found in the execution environment.",
                    throwable
                )
                is InvalidAlgorithmParameterException -> KsPrefEngineException(
                    "The algorithm parameters are invalid.",
                    throwable
                )
                is SignatureException -> KsPrefEngineException(
                    "Generic signature exception.",
                    throwable
                )
                else -> KsPrefEngineException(throwable = throwable)
            }
        }
    }
}