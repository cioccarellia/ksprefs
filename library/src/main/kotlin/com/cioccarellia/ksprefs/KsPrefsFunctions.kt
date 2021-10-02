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
@file:Suppress("unused")

package com.cioccarellia.ksprefs

import com.cioccarellia.ksprefs.delegates.dynamic.DelegateDynamicKsPref
import com.cioccarellia.ksprefs.delegates.dynamic.DelegateDynamicUnsafePref
import com.cioccarellia.ksprefs.extensions.emptyByteArray
import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import kotlin.reflect.KClass

/**
 * Returns a randomly-generated IV
 * */
fun KsPrefs.Companion.randomIV(
    byteCount: Int = 16,
    seed: ByteArray
): ByteArray = SecureRandom(seed).asKotlinRandom().nextBytes(
    emptyByteArray(byteCount)
)

/**
 * Used to initialize a dynamic property.
 * */
fun <T : Any> KsPrefs.dynamic(
    key: String,
    initialization: () -> T
) = DelegateDynamicKsPref(this, key, initialization())


/**
 * Used to initialize a dynamic property.
 * */
fun <T : Any> KsPrefs.dynamic(
    key: String,
    fallback: T
) = DelegateDynamicKsPref(this, key, fallback)


/**
 * Used to initialize an unsafe dynamic property.
 * */
inline fun <reified T : Any> KsPrefs.dynamic(
    key: String
) = DelegateDynamicUnsafePref(this, key, T::class)


/**
 * Used to initialize an unsafe dynamic property.
 * */
fun <T : Any> KsPrefs.dynamic(
    key: String,
    kclass: KClass<T>
) = DelegateDynamicUnsafePref(this, key, kclass)


/**
 * Used to initialize an unsafe dynamic property.
 * */
fun <T : Any> KsPrefs.dynamic(
    key: String,
    jclass: Class<T>
) = DelegateDynamicUnsafePref(this, key, jclass.kotlin)