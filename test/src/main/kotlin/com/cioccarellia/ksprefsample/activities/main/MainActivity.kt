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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.KspConfig
import com.cioccarellia.ksprefsample.App
import com.cioccarellia.ksprefsample.BuildConfig
import com.cioccarellia.ksprefsample.R
import com.cioccarellia.ksprefsample.activities.all.AllActivity
import com.cioccarellia.ksprefsample.activities.ambiguous.AmbiguousActivity
import com.cioccarellia.ksprefsample.activities.batch.BatchActivity
import com.cioccarellia.ksprefsample.activities.dynamic.DynamicActivity
import com.cioccarellia.ksprefsample.activities.json.JsonActivity
import com.cioccarellia.ksprefsample.activities.numbers.NumbersActivity
import com.cioccarellia.ksprefsample.activities.observer.ObserverActivity
import com.cioccarellia.ksprefsample.prefcenter.StartCounterPrefCenter
import com.cioccarellia.ksprefsample.util.onClickDebounced

class MainActivity : AppCompatActivity() {

    private val numbers by lazy { findViewById<Button>(R.id.numbersActivity) }
    private val json by lazy { findViewById<Button>(R.id.jsonActivity) }
    private val ambiguous by lazy { findViewById<Button>(R.id.ambiguosCharActivity) }
    private val batch by lazy { findViewById<Button>(R.id.batchActivity) }
    private val observer by lazy { findViewById<Button>(R.id.observerActivity) }
    private val dynamic by lazy { findViewById<Button>(R.id.dynamicActivity) }
    private val all by lazy { findViewById<Button>(R.id.allActivity) }

    private val detailsTestView by lazy { findViewById<TextView>(R.id.detailsTestView) }

    private val startCount = StartCounterPrefCenter.read()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val destinations = listOf(
            numbers to NumbersActivity::class.java,
            json to JsonActivity::class.java,
            ambiguous to AmbiguousActivity::class.java,
            batch to BatchActivity::class.java,
            observer to ObserverActivity::class.java,
            dynamic to DynamicActivity::class.java,
            all to AllActivity::class.java,
        )

        destinations.forEach { destination ->
            destination.first.onClickDebounced {
                startActivity(
                    Intent(this, destination.second)
                )
            }
        }

        detailsTestView.text = buildString {
            append("KsPrefs ${BuildConfig.VERSION_NAME}")
            append(" ")

            append("@ ${BuildConfig.VERSION_CODE}")
            append(", ")

            append("tgsdk ${com.cioccarellia.ksprefsample.BuildConfig.compileSdk}")
            append(", ")

            append("Kotlin ${com.cioccarellia.ksprefsample.BuildConfig.kotlinVersion}")
            appendLine()

            append(App.prefs.internalReport())
        }



    }

    private fun KsPrefs.internalReport() = buildString {
        val config = KspConfig().apply(App.globalConfigStateFx)

        appendLine("Writing on '$namespace' using ${config.charset}")
        appendLine("SP mode ${config.mode}, strategy ${config.commitStrategy.name}, autosave ${config.autoSave}")

        when (config.encryptionType) {
            EncryptionType.PlainText() -> {
                appendLine("Saved in plaintext")
            }
            EncryptionType.Base64() -> {
                appendLine("Encoded in Base64")
            }
            else -> {
                appendLine("Encrypted using ${config.encryptionType.javaClass.simpleName}")
            }
        }

        appendLine("Start count $startCount")
    }

    private fun log(str: String) = Log.d("KsPref", str)
    private fun toast(str: String) =
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show().also { log(str) }
}