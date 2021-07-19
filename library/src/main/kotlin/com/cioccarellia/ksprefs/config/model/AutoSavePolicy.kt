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
package com.cioccarellia.ksprefs.config.model

enum class AutoSavePolicy {
    /**
     * Updates are propagated automatically to the XML
     * persistent storage immediately.
     * */
    AUTOMATIC,

    /**
     * Changes are not sent to the XML persistent storage
     * and have to be saved manually calling [KsPrefs.save()]
     * */
    MANUAL
}