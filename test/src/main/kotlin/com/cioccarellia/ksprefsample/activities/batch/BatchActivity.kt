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
package com.cioccarellia.ksprefsample.activities.batch

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.ksprefsample.App.Companion.prefs
import com.cioccarellia.ksprefsample.R
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class BatchActivity : AppCompatActivity() {

    private val n = 2_000
    private val k = 30

    private val progressQueue by lazy { findViewById<ProgressBar>(R.id.queueProgress) }
    private val progressPush by lazy { findViewById<ProgressBar>(R.id.pushProgress) }
    private val progressDfu by lazy { findViewById<ProgressBar>(R.id.defaultProgress) }
    private val progressReadDfu by lazy { findViewById<ProgressBar>(R.id.readDfuprogress) }
    private val progressReadKsp by lazy { findViewById<ProgressBar>(R.id.readKspprogress) }

    private val queueTitle by lazy { findViewById<TextView>(R.id.queueTitle) }
    private val pushTitle by lazy { findViewById<TextView>(R.id.pushTitle) }
    private val defaultTitle by lazy { findViewById<TextView>(R.id.defaultTitle) }
    private val readSpTitle by lazy { findViewById<TextView>(R.id.readDfu) }
    private val readKspTitle by lazy { findViewById<TextView>(R.id.readKsp) }

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch)

        title = "KsPrefs ${n / 1000}K Batch"

        progressQueue.max = n
        progressPush.max = n
        progressDfu.max = n
        progressReadDfu.max = k * n
        progressReadKsp.max = k * n

        job = CoroutineScope(Dispatchers.Main).launch {
            val pushTask = async {
                val normalMillis = measureTimeMillis {
                    var lastSampleSec = System.currentTimeMillis()
                    var ops = 0
                    var latestGradient = "N/A"

                    withContext(Dispatchers.Default) {
                        for (i in 0..n) {
                            prefs.push("push-$i", i)
                            withContext(Dispatchers.Main) {
                                progressPush.progress++
                                pushTitle.text = "push() -> $i" + "\n$latestGradient"
                            }

                            val elapsed = System.currentTimeMillis() - lastSampleSec
                            ops++

                            if (elapsed > 1000) {
                                val operationGradient = (ops / (elapsed / 1000))

                                latestGradient = operationGradient.toString() + "op/s"
                                ops = 0
                                lastSampleSec = System.currentTimeMillis()
                            }
                        }
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Normal done, taken ${normalMillis}ms (${normalMillis / 1000}s), avg=${normalMillis.toFloat() / n.toFloat()}task/ms",
                    Toast.LENGTH_LONG
                ).show()

            }

            val queueTask = async {
                val queueMillis = measureTimeMillis {
                    withContext(Dispatchers.Default) {
                        val t1 = System.currentTimeMillis()
                        var lastSampleSec = System.currentTimeMillis()
                        var ops = 0
                        var latestGradient = "N/A"

                        for (i in 0..n) {
                            prefs.queue("queued-$i", i)

                            withContext(Dispatchers.Main) {
                                progressQueue.progress++
                                queueTitle.text = "queue() -> $i"+ "\n$latestGradient"
                            }

                            val elapsed = System.currentTimeMillis() - lastSampleSec
                            ops++

                            if (elapsed > 1000) {
                                val operationGradient = (ops / (elapsed / 1000))

                                latestGradient = operationGradient.toString() + "op/s"
                                ops = 0
                                lastSampleSec = System.currentTimeMillis()
                            }
                        }

                        prefs.save()
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Queue done, taken ${queueMillis}ms (${queueMillis / 1000}s), average ${queueMillis.toFloat() / n.toFloat()}task/ms",
                    Toast.LENGTH_LONG
                ).show()
            }

            val defaultTask = async {
                val dfuMillis = measureTimeMillis {
                    withContext(Dispatchers.Default) {
                        var lastSampleSec = System.currentTimeMillis()
                        var ops = 0
                        var latestGradient = "N/A"

                        for (i in 0..n) {
                            prefs.getSharedPreferences().edit().putString("dfu-$i", i.toString())
                                .apply()

                            withContext(Dispatchers.Main) {
                                progressDfu.progress++
                                defaultTitle.text = "SharedPreferences.edit() -> $i" + "\n$latestGradient"
                            }

                            val elapsed = System.currentTimeMillis() - lastSampleSec
                            ops++

                            if (elapsed > 1000) {
                                val operationGradient = (ops / (elapsed / 1000))

                                latestGradient = operationGradient.toString() + "op/s"
                                ops = 0
                                lastSampleSec = System.currentTimeMillis()
                            }
                        }
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Default done, taken ${dfuMillis}ms (${dfuMillis / 1000}s), average ${dfuMillis.toFloat() / n.toFloat()}task/ms",
                    Toast.LENGTH_LONG
                ).show()
            }


            val readDfu = async {
                var lastSampleSec = System.currentTimeMillis()
                var ops = 0
                var latestGradient = "N/A"

                for (i in 0..n * k) {
                    val y = prefs.getSharedPreferences().getString("dfu-$i", i.toString())

                    withContext(Dispatchers.IO) {
                        progressReadDfu.progress++

                        withContext(Dispatchers.Main.immediate) {
                            readSpTitle.text = "SP.getString($i) -> $y" + "\n$latestGradient"
                        }

                        val elapsed = System.currentTimeMillis() - lastSampleSec
                        ops++

                        if (elapsed > 1000) {
                            val operationGradient = (ops / (elapsed / 1000))

                            latestGradient = operationGradient.toString() + "op/s"
                            ops = 0
                            lastSampleSec = System.currentTimeMillis()
                        }
                    }
                }
            }


            val readKsp = async {

                var lastSampleSec = System.currentTimeMillis()
                var ops = 0
                var latestGradient = "N/A"

                for (i in 0..n * k) {
                    val x = prefs.pull("push-$i", i.toString())

                    withContext(Dispatchers.IO) {
                        progressReadKsp.progress++

                        withContext(Dispatchers.Main.immediate) {
                            readKspTitle.text = "pull($i) -> $x"+ "\n$latestGradient"
                        }

                        val elapsed = System.currentTimeMillis() - lastSampleSec
                        ops++

                        if (elapsed > 1000) {
                            val operationGradient = (ops / (elapsed / 1000))

                            latestGradient = operationGradient.toString() + "op/s"
                            ops = 0
                            lastSampleSec = System.currentTimeMillis()
                        }
                    }
                }
            }


            awaitAll(queueTask, pushTask, defaultTask)

            awaitAll(readDfu, readKsp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        prefs.clear()

        Toast.makeText(
            this@BatchActivity,
            "Clearing all preferences",
            Toast.LENGTH_LONG
        ).show()
    }
}
