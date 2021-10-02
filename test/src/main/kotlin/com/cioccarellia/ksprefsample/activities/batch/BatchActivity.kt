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

    private val n = 10_000

    private val progressQueue by lazy { findViewById<ProgressBar>(R.id.queueProgress) }
    private val progressPush by lazy { findViewById<ProgressBar>(R.id.pushProgress) }
    private val progressDfu by lazy { findViewById<ProgressBar>(R.id.defaultProgress) }

    private val queueTitle by lazy { findViewById<TextView>(R.id.queueTitle) }
    private val pushTitle by lazy { findViewById<TextView>(R.id.pushTitle) }
    private val defaultTitle by lazy { findViewById<TextView>(R.id.defaultTitle) }

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch)

        title = "KsPrefs ${n / 1000}K Batch"

        progressQueue.max = n
        progressPush.max = n
        progressDfu.max = n

        job = CoroutineScope(Dispatchers.Main).launch {
            val pushTask = async {
                val normalMillis = measureTimeMillis {
                    withContext(Dispatchers.Default) {
                        for (i in 0..n) {
                            prefs.push("push-$i", i)
                            withContext(Dispatchers.Main) {
                                progressPush.progress++
                                pushTitle.text = "push() -> $i"
                            }
                        }
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Normal done, taken ${normalMillis}ms (${normalMillis / 1000}s), average ${normalMillis.toFloat() / n.toFloat()}ms/task",
                    Toast.LENGTH_LONG
                ).show()
            }

            val queueTask = async {
                val queueMillis = measureTimeMillis {
                    withContext(Dispatchers.Default) {
                        val t1 = System.currentTimeMillis()

                        for (i in 0..n) {
                            prefs.queue("queued-$i", i)
                            withContext(Dispatchers.Main) {
                                progressQueue.progress++
                                queueTitle.text = "queue() -> $i"

                                if (i == 30) {
                                    val t2 = System.currentTimeMillis()

                                    Toast.makeText(
                                        this@BatchActivity,
                                        "30 tasks, taken ${t2 - t1}ms (${(t2 - t1) / 1000}s), average ${(t2 - t1).toFloat() / 30F}ms/task",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }

                        prefs.save()
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Queue done, taken ${queueMillis}ms (${queueMillis / 1000}s), average ${queueMillis.toFloat() / n.toFloat()}ms/task",
                    Toast.LENGTH_LONG
                ).show()
            }

            val defaultTask = async {
                val dfuMillis = measureTimeMillis {
                    withContext(Dispatchers.Default) {
                        for (i in 0..n) {
                            prefs.expose().edit().putString("dfu-$i", i.toString()).apply()

                            withContext(Dispatchers.Main) {
                                progressDfu.progress++
                                defaultTitle.text = "SharedPreferences.edit() -> $i"
                            }
                        }
                    }
                }

                Toast.makeText(
                    this@BatchActivity,
                    "Default done, taken ${dfuMillis}ms (${dfuMillis / 1000}s), average ${dfuMillis.toFloat() / n.toFloat()}ms/task",
                    Toast.LENGTH_LONG
                ).show()
            }

            awaitAll(queueTask, pushTask, defaultTask)

            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
