# CryptoPrefs
[![Build](https://jitpack.io/v/AndreaCioccarelli/CryptoPrefs.svg)](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs)	
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/LICENSE)	
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b294eaf4988842c090584b1315a5f348)](https://www.codacy.com/app/cioccarelliandrea01/CryptoPrefs)

CryptoPrefs is the new cutting-edge andorid library for storing encrypted preferences securely and protecting them from indiscrete user's eyesights.
All data you are going to store are encrypted using _AES/CBC/PKCS5Padding_ algorithm and wrapped up using standard Base64 encoding.
This library focuses on reliability, security, lightness and speed.

## Repository
CryptoPrefs uses jitpack as packages repository.
To use it you need to add the repository to your project build.gradle file
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

And the dependency to your module build.gradle
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

**Warning #1:** Multiple files support is currently provided, however remember that saving preferences to one single file is much simpler<br>
**Warning #2:** If your project needs an even stronger security layer, consider placing the encryption key in the native libraries. (I personally like [chiper.so](https://github.com/MEiDIK/Cipher.so))


#### Set/Update values
```kotlin
prefs.put("crypto_age", 16)
```
This functions accepts 2 parameters, key and value, that are used to store the preference.
If an item with the matching key is found, its value will be overwritten. Else, a preference is created

The `value` parameter is an `Any` type, it means that it can be everything.

#### Read values
```kotlin
var someValue = prefs.getString("crypto_name", "Andrea")
```

This functions accepts 2 parameters, key and default. Key is used to search the preference into the list, and default is returned if no preference with the matching key is found. This means that if you need to use and create an item you can do it in just one line.

```kotlin
val startCounter = prefs.getInt("start_count", 0)
```



#### Queue operations
```kotlin
for (i in 1..10000) {
    prefs.queue("$i", (i*350).toFloat())
}
prefs.apply()
```


## Standard XML vs CryptoPrefs XML
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
If you wish a full and detailed proof of concept you can look at the :app module of this repository, you will find an android sample about this library and it's advanced functions

## Concept
Android default SharedPreferences APIs allows you to dynamically store some configuration data on your application internal storage. With the time android had become more popular and so many apps were developed. The result is that secure informations, critical/sensitive data and billing informations are stored there [without even a basic protecction](https://medium.com/@andreacioccarelli/android-sharedpreferences-data-weakness-66a44f070e76).

## License
```
CryptoPrefs is licensed under Apache License 2.0 Version 2.0, January 2004
```
