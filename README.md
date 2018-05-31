# CryptoPrefs
[![Build](https://jitpack.io/v/AndreaCioccarelli/CryptoPrefs.svg)](https://jitpack.io/#AndreaCioccarelli/CryptoPrefs)	
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/AndreaCioccarelli/CryptoPrefs/blob/master/LICENSE)	
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b294eaf4988842c090584b1315a5f348)](https://www.codacy.com/app/cioccarelliandrea01/CryptoPrefs)

CryptoPrefs is the new cutting edge andorid library for storing encrypted preferences securely and keeping them apart from indiscrete user's eyesights.
Your keys and values are encrypted using _AES/CBC/PKCS5Padding_ algorithm and wrapped up using standard Base64 encoding. 
This library focus is put on reliability, security, lightness and speed.

## Repository
CryptoPrefs uses jitpack as packages repository.
To use it you need to add this code to your project build.gradle file
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
To use CryptoPrefs you first need to create an istance of this class
```kotlin
val prefs = CryptoPrefs(applicationContext, "CryptoFileName", "c29maWE=")
```
As you can see you need to pass 3 parameters
- The context of your Activity/Fragment
- The file preferences name
- Your secret key

Once your instance is ready the API usage is very simple and effective

#### Put/Update values
```kotlin
prefs.put("crypto_age", 16)
```


#### Read values
```kotlin
var someValue = prefs.get("crypto_name", "Andrea")
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
