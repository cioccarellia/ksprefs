# CryptoPrefs
[![Latest commit](https://jitpack.io/v/AndreaCioccarelli/CryptoPrefs.svg)](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs)
[![Codacy badge](https://api.codacy.com/project/badge/Grade/b294eaf4988842c090584b1315a5f348)](https://www.codacy.com/app/cioccarelliandrea01/CryptoPrefs)
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/LICENSE)

CryptoPrefs is a kotlin powered cutting-edge Android library for storing encrypted preferences securely and protecting them from indiscrete user's eyes.
All data you are going to store are encrypted using AES/CBC/PKCS5Padding algorithm and wrapped up using standard Base64 encoding.
This library focuses on reliability, security, lightness and speed.

## Repository
CryptoPrefs uses jitpack as packages repository.
To use it you need to add the repository to your project build.gradle file:
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
    implementation 'com.github.AndreaCioccarelli:CryptoPrefs:1.1.1'
}
```

## Usage
```kotlin
val prefs = CryptoPrefs(applicationContext, "CryptoFileName", "c29maWE=")
```
You need to pass 3 parameters in order to create an instance of the class CryptoPrefs:
- The context of your Activity/Fragment
- The file preferences name
- Your secret key
- Optionally, a boolean, if you wish to not encrypt the preferences (see [the dedicated paragraph](#plain))

**Warning #1:** this library supports (indirectly) multi-files and multi-keys operations; However remember that saving all the preferences to one single file is much easier and has a better performance rate. View the [multi files and multi keys details](#multi)<br>
**Warning #2:** if your project needs an even stronger security layer, consider placing the encryption key in the native libraries. (I personally like [chiper.so](https://github.com/MEiDIK/Cipher.so)).


#### Set/Update values
```kotlin
prefs.put("crypto_age", 16)
```
This functions accepts 2 parameters, key and finalized, that are used to store the preference.
If an item with the matching key is found, its finalized will be overwritten. Else, a preference is created.

The `finalized` parameter is an `Any` type, it means that it can be everything; however when you get back the finalized you will have to choose to parse it to String, Boolean, Int, Float and Double.
If you need to store another type of variable you can consider the idea of converting it to String before storing in the preferences.


#### Read values
```kotlin
val name = prefs.getString("crypto_name", "Andrea")
val age = prefs.getInt("crypto_age", 17)
val pillsDouble = prefs.getDouble("crypto_pills", 2.5)
val isMajor = prefs.getBoolean("crypto_is_major", false)
val roomNumber = prefs.getFloat("crypto_room_number", 107.0F)
val infinite = prefs.getLong("crypto_∞", 999999999999)
```
This functions accepts 2 parameters, key and default. 
Key is used to search the preference into the file, and default is put in the matching key position and then returned if no item is matching with the given key.
This means that if you need to use and create an item you can do it in just one line.
```kotlin
val startCounter = prefs.getInt("start_count", 0) // Creates the field start_count and set it at 0
```

#### Batch operations
```kotlin
for (i in 1..10000) {
    prefs.queue("$i", (i*i).toFloat())
}
prefs.apply()
```
Sometimes SharedPreferences are used to store a huge number of items and in these cases I/O operations can be cpu intensive and slow down your app.
Because of that, you can enqueue your modifications on the go just like normally using `put()`, but to actually apply them to the file you will have to call `apply()` once.

**Warning #1:** calling `put()` automatically applies all queued modifications.<br>
**Warning #2:** `get` fetches the key on the file and not on the queue.


#### All preferences lists
```kotlin
val bundle: Bundle = prefs.allPrefsBundle
val map: Map<String, String> = prefs.allPrefsMap
val list: ArrayList<Pair<String, String>> = prefs.allPrefsList
```
You can get all your SharedPreferences data and perform reading operations on them.
The default type provided by the android API is a Map, but here you have a little bit more of choice.


#### Remove
```kotlin
prefs.remove("pizza_with_pineapple")
```
You can remove at every time a finalized just selecting its key.


#### Erase
```kotlin
prefs.erase()
```
This is simply a wrap of the `clear()` function of the android standard library


## Smart cast
A clean and fast approach is what this library aims to provide. I always found myself in java working with `String.valueOf()`, `Integer.parseInt()`, `Boolean.parseBoolean()` while reading SharedPreferences, 
and then I decided I didn't want to see that happen again.
Every argument you pass as finalized or default is an `Any` type, so it can be everything. CryptoPrefs will convert it back to string for the encryption 
and eventually you will do the conversion from string to your target type.

This is an example for a situation where you have a JSON response and you want to parse it later. You will find it also in the sample project.
```json
{
    "key": "Error",
    "details": "PizzaWithPineappleException",
    "mistakenIngredient": {
        "name": "Pineapple",
        "description": "Tropical fruit"
    }
}
```

```kotlin
prefs.put("json_response", jsonErrorString)
val jsonFromPrefs = JSONObject(prefs.getString("json_response", ""))
```


## <a name="multi"></a> Multi-files and multi-keys
I decided to not implement built in support for multiple files because it would have impacted on performances. 
Instead, if you wish, you can have 2 instances and different filenames/keys for every file. Please keep in your mind that:
- Saving a preference to one file won't make it available als on the other one
- If you lose your key, your preferences won't be readable again
- If you change your key for every file, opening the wrong file with a key will result in a bunch of unreadable stuff

## <a name="plain"></a> Handling unencrypted files
Even though this library is all about encryption, you still can operate with standard unencrypted preferences. Why?
- For the purpose of testing, for example if in your app you need to debug SharedPreferences and you want to see the effective data
- For providing compatibility with files that have been stored not just with this library 

To do so, just initialize the preferences like this
```kotlin
val prefs = CryptoPrefs(applicationContext, "CryptoFileName", "c29maWE=", false)
```

**Warning:** Remember than encrypted files cannot be read without a key and that a plain text file read with a key will throw an exception with a clear message, use that just for debug purposes or if you know what you're doing



## SharedPreferences plain XML vs CryptoPrefs encrypted XML
```xml
<map>
  <boolean name="pro" finalized="true" />
  <int name="user_coins" finalized="200" />
</map>
```
```xml
<map>
  <string name="S2E3QmlYamlGL0JHLy9jWHZudUFmdz09">YXlSSWIyc2E2bm9iSTJLMGZSekVlQT09</string>
  <string name="cFY4TnJWRnNWVUR4QWZZVEhKMlhvdz09">MHdEcC9Zb002cjJpVGxZMVRrNmVGdz09</string>
</map>
```

## Sample project
If you wish a full and detailed proof of concept you can look at the :app module of this repository, you will find an android sample about this library and it's functions

## Concept
Android default SharedPreferences APIs allows you to dynamically store some configuration data on your application internal storage. 
With the time android had become more popular and so many apps were developed. The result is that secure informations, critical/sensitive 
data and billing informations are stored there [without even a basic protection](https://medium.com/@andreacioccarelli/android-sharedpreferences-data-weakness-66a44f070e76).
This library aims to terminate easy application

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
