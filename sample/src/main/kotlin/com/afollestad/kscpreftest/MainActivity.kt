/**
 * Designed and developed by Aidan Follestad (@afollestad)
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
package com.afollestad.librarytemplatesample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.librarytemplate.Greeter

class MainActivity : AppCompatActivity() {
  private val inputView by lazy { findViewById<TextView>(R.id.inputView) }
  private val buttonView by lazy { findViewById<Button>(R.id.buttonView) }
  private var greeter: Greeter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    greeter = Greeter(this)

    buttonView.onClickDebounced {
      AlertDialog.Builder(this)
        .setMessage(greeter?.greet(inputView.text.toString()))
        .show()
    }
  }

  override fun onDestroy() {
    greeter?.dispose()
    greeter = null
    super.onDestroy()
  }
}
