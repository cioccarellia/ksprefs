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
package com.afollestad.librarytemplate

import android.content.Context
import android.content.res.Resources
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class GreeterTest {
  private val resources = mock<Resources> {
    on { getString(eq(R.string.hello_x), isA()) } doAnswer { inv ->
      val name: String = inv.getArgument(1)
      "Hello, $name!"
    }
  }
  private val context = mock<Context> {
    on { resources } doReturn resources
  }
  private val greeter = Greeter(context)

  @Test fun greet() {
    val name = "Aidan"
    assertThat(greeter.greet(name)).isEqualTo("Hello, $name!")
  }

  @Test fun dispose() {
    assertThat(greeter.context).isNotNull()
    greeter.dispose()
    assertThat(greeter.context).isNull()
    assertThat(greeter.greet("Any name")).isEmpty()
  }
}
