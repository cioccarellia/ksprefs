# CryptoPrefs
[![Build](https://jitpack.io/v/AndreaCioccarelli/CryptoPrefs.svg)](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs)	
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/LICENSE)	
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b294eaf4988842c090584b1315a5f348)](https://www.codacy.com/app/cioccarelliandrea01/CryptoPrefs)

CryptoPrefs is the new kotlin powered cutting-edge andorid library for storing encrypted preferences securely and protecting them from indiscrete user's eyesights.
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
    implementation 'com.github.AndreaCioccarelli:CryptoPrefs:1.0.0'
}
```

## Usage
```kotlin
val prefs = CryptoPrefs(applicationContext, "CryptoFileName", "c29maWE=")
```
You need to pass 3 parameters in order to create an istance of the class CryptoPrefs:
- The context of your Activity/Fragment
- The file preferences name
- Your secret key

**Warning #1:** multiple files support is currently provided, however remember that saving preferences to one single file is much easier.<br>
**Warning #2:** if your project needs an even stronger security layer, consider placing the encryption key in the native libraries. (I personally like [chiper.so](https://github.com/MEiDIK/Cipher.so)).


#### Set/Update values
```kotlin
prefs.put("crypto_age", 16)
```
This functions accepts 2 parameters, key and value, that are used to store the preference.
If an item with the matching key is found, its value will be overwritten. Else, a preference is created.

The `value` parameter is an `Any` type, it means that it can be everything; however when you get back the value you will have to choose to parse it to String, Boolean, Int, Float and Double.
If you need to store another type of variable you can consider the idea of converting it to String before storing in the preferences.


#### Read values
```kotlin
var someValue = prefs.getString("crypto_name", "Andrea")
```
This functions accepts 2 parameters, key and default. 
Key is used to search the preference into the file, and default is put in the matching key position and then returned if no item is matching with the given key.
This means that if you need to use and create an item you can do it in just one line.
```kotlin
val startCounter = prefs.getInt("start_count", 0) // Creates the field start_count and set it at 0
```

#### Queue operations
```kotlin
for (i in 1..10000) {
    prefs.queue("$i", (i*i).toFloat())
}
prefs.apply()
```
Sometimes SharedPreferences are used to store a huge number of items and in these cases I/O operations can be cpu intensive and slow down your main thread.
Because of that, you can enqueue your modifications on the go just like normally using `put()`, but to actually apply them to the file you will have to call `apply()` once.

**Warning #1:** calling `put()` means that all the queued modifications will be applied.
**Warning #2:** `get` fetches the key on the file and not on the queue.


#### Batch operations
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
You can remove at every time a value just selecting its key.


#### Erase
```kotlin
prefs.erase()
```
This is simply a wrap of the `clear()` function of the android standard library


## SharedPreferences plain text XML vs CryptoPrefs encrypted XML
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
If you wish a full and detailed proof of concept you can look at the :app module of this repository, you will find an android sample about this library and it's functions

## Concept
Android default SharedPreferences APIs allows you to dynamically store some configuration data on your application internal storage. With the time android had become more popular and so many apps were developed. The result is that secure informations, critical/sensitive data and billing informations are stored there [without even a basic protection](https://medium.com/@andreacioccarelli/android-sharedpreferences-data-weakness-66a44f070e76).
This library aims to terminate easy application

## License
```
CryptoPrefs is licensed under Apache License 2.0 Version 2.0
```
