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
package com.cioccarellia.ksprefsample.activities.observer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.kspref.observe
import com.cioccarellia.ksprefsample.App.Companion.prefs
import com.cioccarellia.ksprefsample.R
import kotlin.random.Random

class ObserverActivity : AppCompatActivity() {

    private val log by lazy { findViewById<TextView>(R.id.log) }

    private var observed1 by prefs.observe("test_observer1", 1) { old, new ->
        log.text = log.text.toString() + "Old 1: $old\nNew 1: $new\n"
    }

    private var observed2 by prefs.observe("test_observer2", 2) { old, new ->
        log.text = log.text.toString() + "Old 2: $old\nNew 2: $new\n"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observer)

        observed1 = Random.nextInt()
        observed2 = Random.nextInt()
    }
}
