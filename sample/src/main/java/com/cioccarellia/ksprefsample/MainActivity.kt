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
package com.cioccarellia.ksprefsample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.kspref.KsPrefs
import com.cioccarellia.kspref.config.AutoSavePolicy
import com.cioccarellia.kspref.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val inputView by lazy { findViewById<TextView>(R.id.inputView) }
    private val buttonView by lazy { findViewById<Button>(R.id.buttonView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = KsPrefs(baseContext) {
            autoSave = AutoSavePolicy.AUTO
            encryption.initPlainText()
        }

        var value by prefs.observe("randomString", "HEY") { oldValue, newValue ->
            toast("Previous: -> '$oldValue'\nNew -> '$newValue'")
        }

        buttonView.onClickDebounced {
            value = "likguyt"

            if (Random.nextBoolean()) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(5_000)
                    prefs.push("randomString", "reset!!! TITI HAHA")
                }
            }
        }
    }

    private fun log(str: String) = Log.d("KsPref", str)
    private fun toast(str: String) =
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show().also { log(str) }
}