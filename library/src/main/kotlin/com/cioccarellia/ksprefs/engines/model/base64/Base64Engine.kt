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
package com.cioccarellia.ksprefs.engines.model.base64

import android.util.Base64
import com.cioccarellia.ksprefs.engines.Transmission
import com.cioccarellia.ksprefs.engines.base.Engine

internal class Base64Engine(
    private val base64Flags: Int
) : Engine() {
    override fun derive(incoming: Transmission) = Transmission(
        Base64.encode(incoming.payload, base64Flags)
    )

    override fun integrate(outgoing: Transmission) = Transmission(
        Base64.decode(outgoing.payload, base64Flags)
    )
}