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
package com.cioccarellia.ksprefsample.activities.json

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefsample.App
import com.cioccarellia.ksprefsample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class JsonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)

        App.prefs.push(
            "pizza_error", JSONObject(
                """
                {
                    "code": "Enrico Toti, 18",
                    "exception_details": {
                        "name": "PizzaOnPineappleException",
                        "parent": "TropicalFruitOnPizzaException",
                        "description": "🤌🇮🇹"
                    }
                }
            """.trimIndent()
            )
        )

        Toast.makeText(
            this,
            App.prefs.pull<JSONObject>("pizza_error")
                .getJSONObject("exception_details")
                .getString("description"),
            Toast.LENGTH_LONG
        ).show()


/*
        // multithreading tests
        for (i in 1..1000){
            CoroutineScope(Dispatchers.Default).launch() {
                App.prefs.push("xxx", true)
                val isEnabled = App.prefs.pull("xxx", true)
                Log.d("Testing", isEnabled.toString())
            }
        }



        for (i in 1..1000){
            CoroutineScope(Dispatchers.Default).launch() {
                App.prefs.push("xxx", false)
                val isEnabled = App.prefs.pull("xxx", true)
                Log.d("Testing", isEnabled.toString())
            }
        }
*/

        finish()
    }
}
