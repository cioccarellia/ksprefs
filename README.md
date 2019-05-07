# CryptoPrefs
[![Latest Release](https://jitpack.io/v/AndreaCioccarelli/CryptoPrefs.svg)](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b294eaf4988842c090584b1315a5f348)](https://www.codacy.com/app/cioccarelliandrea01/CryptoPrefs)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CryptoPrefs-green.svg?style=flat )]( https://android-arsenal.com/details/1/7009)
[![Language](https://img.shields.io/badge/language-kotlin-orange.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/library/build.gradle)
[![Min sdk](https://img.shields.io/badge/minsdk-14-yellow.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/library/build.gradle)
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/LICENSE)

CryptoPrefs is a stable, kotlin powered, cutting-edge Android library for storing encrypted preferences securely.
The preference list is encrypted using AES256+Base64 algorithms and stored on a XML protected file, 
This library is focused on reliability, security, lightness and speed.

## Repository
CryptoPrefs uses [jitpack](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs) as package repository.
To use it you need to add that line to your project build.gradle file:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
And the dependency to your module build.gradle file:
```gradle
dependencies {
    implementation 'com.github.AndreaCioccarelli:CryptoPrefs:1.3.2.5'
}
```

## Usage
```kotlin
val prefs = CryptoPrefs(context, "CryptoFileName", "c29maWE=")
```
You need to pass 3 parameters in order to create a CryptoPrefs instance:
- The context of your Application/Activity
- The file name, used to read/write the preferences
- An encryption key

**Warning #1:** this library supports (indirectly) multi-files and multi-keys operations; however saving all the preferences to one single file is much easier and has a better performance rate. View the [multi files and multi keys details](#multi)<br>
**Warning #2:** if your project needs an even stronger security layer, consider hiding the encryption key in a bundled native library that you'll ship with your app, so that a full decryption will be made extremely difficult. (e.g. [chiper.so](https://github.com/MEiDIK/Cipher.so)).


#### Put/Get values
```kotlin
prefs.put("crypto_age", 17)
```
This method requires 2 parameters, `key` and `value`, that are used to store the preference.
If an item with the matching key is found, its value will be overwritten. Otherwise, a new preference item is created.

The `value` parameter is specified as `Any` (java Object), this means that it can be everything. However when you retrieve the preference the result will be converted in the type you used to define the default value in the `get()` call.
This means that you have to pay attention at this type inference while coding, because setting a preference as a string and fetching it later as an integer will obviously raise a conversion exception.
If you need to store a custom type you can consider the idea of explicitly converting it to String. Thereby, you can define an extension function to create a custom parser for that specific type (e.g. get and put JSON objects using Gson).


#### Typed values
```kotlin
val name = prefs.get("crypto_name", "Andrea")               // (String)
val age = prefs.get("crypto_age", 17)                       // (Int)
val pillsDouble = prefs.get("crypto_pills", 2.5)            // (Float)
val isMajor = prefs.get("crypto_is_major", false)           // (Boolean)
val roomNumber = prefs.get("crypto_room_number", 107.0F)    // (Float)
val infinite = prefs.get("crypto_∞", 9223372036854775807)   // (Long)
```
As mentioned earlier, the `get()` function accepts 2 parameters, `key` and `default`. 
This generic method fetches the value stored on the preference file and it converts it to the inferred type (determined using the default value parameter.
Under the hood, the preference file is scanned by the android native preference manager and if an entry matches with the given key, its value is decrypted and returned. Otherwise the preference is created with the `default` argument as value.
This means that if you need to retrive the preference value if it exists and otherwise to create it (Common situation while developing Android apps) you can do it using just one instruction.
The built-in supported types are `String`, `Boolean`, `Int`, `Short`, `Long`, `Float`, `Double` and `Byte`.
```kotlin
val startCounter = prefs.get("start_count", 0) // Creates the field "start_count" and sets it to 0
```

#### Batch operations
```kotlin
for (i in 1..100_000) {
    prefs.enqueue("$i", (i*i + 1).toLong())
}
prefs.apply()
```
Sometimes SharedPreferences are used to store a very large number of values and in those scenarios I/O operations can be CPU intensive and consequently slow down your app, since every operation is executed on the main thread.
In the above scenario, you should enqueue your modifications using `enqueue()` (the usage is the same as `put()`), but you will actually apply your edits to the file with `apply()` when you are done.
You can think of it as if you were drafting your modifications without any write operation, and then you will make your modifications persistent by applying your changes, with a fresh speed boost.

**Warning #1:** invoking `put()` automatically applies all the enqueued modifications.<br>
**Warning #2:** `get()` fetches the values that are actually present in the file, and not on the modification queue.


#### Preference lists
```kotlin
val list = prefs.getAll()
```
You can grab the preference list via this function to perform batch operations.
The default type provided by the android API is `Map<String, Any?>`, but with CryptoPrefs you will find `Map<String, String>`.

#### Remove
```kotlin
prefs.remove("pizza_with_pineapple")
```
You can remove a record by passing its key to `remove()`. 
If no item with the matching key is found nothing happens.

#### Erase
```kotlin
prefs.erase()
```
This a simple wrap of the `clear()` method of the android standard library, so what it does is deleting the whole file content, with a more proper name. Use with caution.


## Smart type casting & extensibility
A clean and fast approach is what this library aims to provide. Every java developer found him/herself working with stuff like `String.valueOf()`, `Integer.parseInt()`, `Boolean.parseBoolean()` while dealing with SharedPreferences, and then I decided I didn't want to see that happen again with Kotlin.
Every argument you pass as the second parameter of any I/O function (`put(k,v)`) is an `Any` type. CryptoPrefs will convert it to String for the encryption and eventually you will have to perform the conversion from string to your target type when reading the value.

This is an example for a situation where you have a JSON response and you want to store it (And supposedly parse it later on). You will find this piece of code also in the sample project.
```json
{
    "status": "Error",
    "exception": "PizzaWithTropicalFruitException",
    "debug": {
        "mistakenIngredient": "Pineapple",
        "crashDescription": "Tropical fruits on pizza throw an exception"
    }
}
```

Here we have the usage details: on the first snippet the response is written and on the second one it's read.
```kotlin
prefs.put("json_response", jsonErrorString)
```

```kotlin
val jsonFromPrefs = JSONObject(prefs.get("json_response", ""))
```

As mentioned before, you can create an extension function if you are using Kotlin. It'd simplify the way you interact with your preferences, because by doing so you can store and fetch custom types for your specific app architecture. 
Let's suppose that you are using a class called `Pizza` to parse a JSON response with gson. To save it (e.g. for offline use) you just have to write one extension function to parse it.

```kotlin
fun CryptoPrefs.getPizza(key: String, default: String): Pizza {
    val json = preferences.get(key, default).toString()
    return Gson().fromJson(json, Pizza::class.java)
}
```

On the other side, you can extend this library to create your own methods and simplify your code.
For example, let's take a very common pattern
```kotlin
preferences.put("startCount", preferences.get("startCount", 0) + 1);
val count = preferences.get("startCount", 0)
```
The code shown above is used to increment a counter every time the user starts the app, but the syntax is unclear and hard to read.

```kotlin
fun CryptoPrefs.incrementCounter(key: String, default: String): Int {
    val count = preferences.readCounter() + 1
    preferences.put("startCount", count)
    return count
}

fun CryptoPrefs.readCounter() = preferences.get("startCount", 0)
```
Like that you can invoke `preferences.incrementCounter()` and the work is done for you, you have the number returned and incremented easily with one line of code.

## <a name="multi"></a> Multi-files and Multi-keys
This library does not provide built-in support for multiple files, as it would have slightly impacted performances. 
Instead, you can have 2 instances and different filenames/keys for every file, that actually is the best solution for logical division and performances.
Please keep in mind that:
- Saving a preference to one file won't make it available also on the other one
- If you lose your encryption key, your preferences won't be readable again
- Opening a file with the wrong decryption key will result in a bunch of unreadable stuff

## <a name="plain"></a> Handling unencrypted files
Even though this library is about encryption, you still can operate with standard unencrypted preferences. It is done for:
- For the purpose of testing, for example if in your app you need to debug SharedPreferences and you want to see the effective data
- To provide compatibility with files that have been stored in the past without decryption
- To provide compatibility with files that have been created using android settings, that does not use encryption

To do so, you have to initialize the instance like this
```kotlin
val prefs = CryptoPrefs(applicationContext, "CryptoFileName", "c29maWE=", false)
```

**Warning:** Remember than encrypted files cannot be read without a key and that a plain text file read with a key will throw an exception with a clear message: use that if you know what you're doing.


## SharedPreferences plain XML vs CryptoPrefs encrypted XML
```xml
<map>
  <boolean name="pro" value="true" />
  <int name="user_coins" value="200" />
</map>
```
```xml
<map>
  <string name="S2E3QmlYamlGL0JHLy9jWHZudUFmdz09">YXlSSWIyc2E2bm9iSTJLMGZSekVlQT09</string>
  <string name="cFY4TnJWRnNWVUR4QWZZVEhKMlhvdz09">MHdEcC9Zb002cjJpVGxZMVRrNmVGdz09</string>
</map>
```

## Sample project
If you need a complete and detailed PoC with code examples you can check out the :app module of this repository.

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
