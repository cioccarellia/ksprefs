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
package com.cioccarellia.ksprefsample.activities.numbers

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.cioccarellia.ksprefsample.App
import com.cioccarellia.ksprefsample.R
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class NumbersActivity : AppCompatActivity() {

    private val log by lazy { findViewById<TextView>(R.id.log) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers)

        val t = measureTimeMillis {
            App.prefs.push("int", 1)
            App.prefs.push("long", 3141592653589793238)
            App.prefs.push("short", Short.MAX_VALUE)
            App.prefs.push("float", 1.41421356237309504880F)
            App.prefs.push("double", 2.71828182845904523536028747135266)
            App.prefs.push(
                "bigint",
                BigInteger(8 * 8 * 8 * 2, java.util.Random(System.currentTimeMillis()))
            )
            App.prefs.push("enum", CommitStrategy.APPLY)

            log.text = buildString {
                val int = App.prefs.pull<Int>("int")
                val long = App.prefs.pull<Long>("long")
                val short = App.prefs.pull<Short>("short")
                val float = App.prefs.pull<Float>("float")
                val double = App.prefs.pull<Double>("double")
                val bigint = App.prefs.pull<BigInteger>("bigint")
                val enum = App.prefs.pull<CommitStrategy>("enum")

                listOf(int, long, short, float, double, bigint, enum).forEach {
                    appendLine("${it::class.simpleName} -> $it")
                }
            }
        }

        log.append("\n[elapsed: ${t}ms]")
    }
}
