<p align="center">
  <a href="https://github.com/cioccarellia/ksprefs" target="_blank"><img width="100" src="extras/ksprefs.png"></a>
</p>
<h1 align="center">KsPrefs</h1>
<p align="center">Simplify SharedPreferences. 100% Kotlin.</p>
<p align="center">
  <a tagret="_blank" href="https://bintray.com/cioccarellia/kurl/kurl/_latestVersion"><img src="https://api.bintray.com/packages/cioccarellia/maven/ksprefs/images/download.svg" alt="Download from Bintray"></a>
  <a tagret="_blank" href="https://app.circleci.com/pipelines/github/cioccarellia/ksprefs"><img src="https://circleci.com/gh/cioccarellia/ksprefs.svg?style=svg" alt="CircleCI"></a>
  <a tagret="_blank" href="https://app.codacy.com/manual/cioccarellia/ksprefs/dashboard"><img src="https://api.codacy.com/project/badge/Grade/23db3b5c2d8647af86b309dd75f7393d" alt="Codacy"></a>
  <a><img src="https://img.shields.io/badge/kotlin-1.3.72-orange.svg" alt="Kotlin"></a>
  <a><img src="https://img.shields.io/badge/min-19-00e676.svg" alt="Android Min Sdk"></a>
  <a><img src="https://img.shields.io/badge/compile-29-00e676.svg" alt="Android Compile Version"></a>
  <a href="https://github.com/cioccarellia/ksprefs/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg" alt="License"></a>
</p>

### TLDR
```gradle
implementation 'com.cioccarellia.ksprefs:$version'
```

- :zap: Powerful SharedPreferences wrapper.
- :rocket: Easy to pick up & use right away for any project.
- :gear: Fully customizable behaviour.
- :lock: Built-in cryptography (PlainText, Base64, AES CBC, AES ECB, Android Keystore + AES GCM).
- :symbols: Extensive type support.
- :heart: Kotlin powered.

```kotlin
val prefs = KsPrefs(applicationContext)
```

To read from SharedPreferences, use `pull(key, default)`.<br>
To write to SharedPreferences, use `push(key, value)`.

## Introduction
<img src="extras/light/png/scheme.png"><br><br>
KsPrefs (<b>K</b>otlin <b>S</b>hared <b>Pref</b>erences) is a wrapper for the default Android SharedPreferences implementation.
KsPrefs goal is to bring Kotlin & cryptography advanced features and standards on any Android codebase. 
As a secondary purpose, this is meant as a replacement for the default SharedPreference API which lacks extensibility, customizability, security & coinciseness.
This library is different from its baseline because it creates and adds extra functionality, providing control and extensibility to the default API, to create a preference store with the following properties:
- Strongly-typed
- Null-safe & fail-safe reads
- Encrypted
- Optimized
- Customizable
- Non-verbose
- Extensible
- Open Source

## Functionality
### Constructor
You should have only one `KsPrefs` instance among your codebase. 
```kotlin
val prefs = KsPrefs(applicationContext)
```
It is recommanded to keep it inside your `Application` instance, so that it's accessible anywhere in code.
```kotlin
class App : Application() {

    companion object {
        lateinit var appContext: Context
        val prefs by lazy { KsPrefs(appContext) }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}
```


### Config
A lot of times, KsPrefs takes one or another action by viewing at the content of the configuration.<br>
To configure KsPrefs, you just pass in a lambda which edits the configuration deaults.

```kotlin
val prefs = KsPrefs(applicationContext) {
    autoSavePolicy = AutoSavePolicy.MANUAL
    commitStrategy = CommitStrategy.COMMIT
}
```

| Field | Type | Description | Default Value |
|-----------------|----------------|--------------------------------------------------------------------------------------------|----------------------|
| mode | Int | SharedPreferences access mode | Context.MODE_PRIVATE |
| charset | Charset | Charset used for string-to-byte and byte-to-string conversions | Charsets.UTF_8 |
| autoSave | AutoSavePolicy | Whether after a `push()` operation a commit is executed according to the `commitStrategy` | AutoSavePolicy.AUTO |
| commitStrategy | CommitStrategy | Which strategy to use at the moment of writing the preferences onto the persistent storage | CommitStrategy.APPLY |
| keyRegex | Regex? | Regex which, if non null, every key must match. | null |
| encryptionType | EncryptionType | Byte transformation technique used to derive and integrate data upon storage operations | PlainText |
| keySizeMismatch | KeySizeMFS | Action to be taken when the supplied encryption key does not match its expected size | CRASH |

### Read
To retrieve values from the preference storage you can use `pull()`.<br>
Pull comes in 4 variants, 3 of which are unsafe.
A variant is defined *safe* is when you also supply the fallback (default) value, so that, for any given key, you always have a concrete value to return.

```kotlin
val safePull = prefs.pull("username", "nobody")
```

Even though the standard Android SharedPreferences API forces you to provide a default value, KsPrefs lets you leave that blank, as supplying an actual instance of an object may get verbose, and pointless if you are sure the key is present inside the storage.

```kotlin
val username = prefs.pull<String>("username")
val usernameInferred: String = prefs.pull("username")
```
*:pushpin: Other functions accept the type parameter as a class or as a generic. On the latter, the bytecode of the function is inlined, in order to allow the generic type to be reified.*

### Write
To save values to the preverence storage you can use `push()`<br>
Push takes the key and the value, and stores them inside the preferences.

```kotlin
prefs.push("username", viewModel.username)
```

### Save
To commit any pending transaction to the storage, you can use `save()`<br>
By default, the commit strategy saves the value in-memory and 


### Queuing
To enqueue values to be written onto the preference storage you can use `queue()`<br>
Queue takes the key and the value, and saves the changes in-memory, but does not actually write them to the .

```kotlin
for ((index, pic) in picArray.withIndex()) {
    prefs.queue("pic-$index", pic.url)
}

prefs.save()
```
