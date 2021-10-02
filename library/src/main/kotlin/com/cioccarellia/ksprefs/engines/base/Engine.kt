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
package com.cioccarellia.ksprefs.engines.base

import com.cioccarellia.ksprefs.annotations.Derivative
import com.cioccarellia.ksprefs.annotations.Integral
import com.cioccarellia.ksprefs.engines.Transmission
import com.cioccarellia.ksprefs.extensions.bytes
import com.cioccarellia.ksprefs.internal.SafeRun

@PublishedApi
internal abstract class Engine : SafeRun {
    @Derivative
    abstract fun derive(incoming: Transmission): Transmission

    @Integral
    abstract fun integrate(outgoing: Transmission): Transmission

    fun derive(incoming: String) = derive(
        Transmission(
            incoming.bytes()
        )
    ).toString()

    fun integrate(incoming: String) = integrate(
        Transmission(
            incoming.bytes()
        )
    ).toString()
}