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
package com.cioccarellia.ksprefsample.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefsample.R
import com.cioccarellia.ksprefsample.activities.ambiguous.AmbiguousActivity
import com.cioccarellia.ksprefsample.activities.batch.BatchActivity
import com.cioccarellia.ksprefsample.activities.json.JsonActivity
import com.cioccarellia.ksprefsample.activities.numbers.NumbersActivity
import com.cioccarellia.ksprefsample.util.onClickDebounced

class MainActivity : AppCompatActivity() {
    private val numbers by lazy { findViewById<Button>(R.id.numbersActivity) }
    private val json by lazy { findViewById<Button>(R.id.jsonActivity) }
    private val ambiguous by lazy { findViewById<Button>(R.id.ambiguosCharActivity) }
    private val batch by lazy { findViewById<Button>(R.id.batchActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numbers.onClickDebounced {
            startActivity(
                Intent(this, NumbersActivity::class.java)
            )
        }

        json.onClickDebounced {
            startActivity(
                Intent(this, JsonActivity::class.java)
            )
        }

        ambiguous.onClickDebounced {
            startActivity(
                Intent(this, AmbiguousActivity::class.java)
            )
        }

        batch.onClickDebounced {
            startActivity(
                Intent(this, BatchActivity::class.java)
            )
        }
    }

    private fun log(str: String) = Log.d("KsPref", str)
    private fun toast(str: String) =
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show().also { log(str) }
}