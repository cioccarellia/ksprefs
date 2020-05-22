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
package com.cioccarellia.ksprefsample.activities.dynamic

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefs.dynamic
import com.cioccarellia.ksprefsample.App.Companion.prefs
import com.cioccarellia.ksprefsample.R
import kotlin.random.Random

class DynamicActivity : AppCompatActivity() {

    private val log by lazy { findViewById<TextView>(R.id.log) }

    private var dynamic1 by prefs.dynamic<Int>("just_one_dynamic_prop_1")
    private var dynamic2 by prefs.dynamic<Int>("just_one_dynamic_prop_2")
    private var dynamic3 by prefs.dynamic<Int>("just_one_dynamic_prop_3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        dynamic1 = Random.nextInt()
        dynamic2 = Random.nextInt()
        dynamic3 = Random.nextInt()

        log.text = "Dynamic 1: $dynamic1\nDynamic 2: $dynamic2\nDynamic 3: $dynamic3"
    }
}
