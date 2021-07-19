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

enum class CommitStrategy {
    /**
     * Asynchronous.
     * Safe to ignore return value, faster.
     * Updates the global SharedPreference in-memory values.
     * */
    APPLY,

    /**
     * Synchronous.
     * Atomically performs the operation, slower.
     * Safe for multi threaded applications.
     * */
    COMMIT,

    /**
     * Nothing is done when this option is chosen.
     * This should never be your configuration default, but
     * it may happen to be a parameter you can pass to [push()]
     * to avoid writing the change back to the storage
     * and to keep it stashed in memory.
     * */
    NONE
}