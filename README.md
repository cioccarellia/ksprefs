<p align="center">
  <a href="https://github.com/cioccarellia/ksprefs" target="_blank"><img width="100" src="extras/ksprefs.png"></a>
</p>
<h1 align="center">KsPrefs</h1>
<p align="center">Simplify SharedPreferences. 100% Kotlin.</p>
<p align="center">
  <a tagret="_blank" href="https://bintray.com/cioccarellia/maven/ksprefs/_latestVersion"><img src="https://api.bintray.com/packages/cioccarellia/maven/ksprefs/images/download.svg" alt="Download from Bintray"></a>
  <a tagret="_blank" href="https://app.circleci.com/pipelines/github/cioccarellia/ksprefs"><img src="https://circleci.com/gh/cioccarellia/ksprefs.svg?style=svg" alt="CircleCI"></a>
  <a tagret="_blank" href="https://app.codacy.com/manual/cioccarellia/ksprefs/dashboard"><img src="https://api.codacy.com/project/badge/Grade/f10cdbdbe7b84d0ea7a03b755c104e03" alt="Codacy"></a>
  <a><img src="https://img.shields.io/badge/kotlin-1.3.72-orange.svg" alt="Kotlin"></a>
  <a><img src="https://img.shields.io/badge/min-19-00e676.svg" alt="Android Min Sdk"></a>
  <a><img src="https://img.shields.io/badge/compile-29-00e676.svg" alt="Android Compile Version"></a>
  <a href="https://github.com/cioccarellia/ksprefs/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg" alt="License"></a>
</p>

### TLDR
```gradle
implementation 'com.cioccarellia:ksprefs:2.0.0-rc3'
```

- :zap: Powerful SharedPreferences wrapper.
- :rocket: Easy to pick up & use right away for any project.
- :gear: Fully customizable behaviour.
- :lock: Built-in cryptography (PlainText, Base64, AES CBC, AES ECB, Android KeyStore + AES GCM / + RSA).
- :symbols: Extensive type support.
- :heart: Kotlin powered.

```kotlin
val prefs = KsPrefs(applicationContext)
```

To _read_ from SharedPreferences, use `pull(key, fallback)`.<br>
To _write_ to SharedPreferences, use `push(key, value)`.

## Introduction
<img src="extras/light/png/scheme.png"><br><br>
KsPrefs (<b>K</b>otlin <b>S</b>hared <b>Pref</b>erences) is a wrapper for the default Android SharedPreferences implementation.
KsPrefs goal is to bring both Kotlin & cryptography advanced features and standards on any Android app. 
As a secondary purpose, this is meant as a replacement for the default SharedPreference API which lacks extensibility, customizability, security & conciseness.
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

It is recommended to keep it inside your `Application` class, so that it's accessible anywhere in code.

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

### Configuration
KsPrefs acts depending on the content of its configuration.<br>
To configure KsPrefs, you have to pass in a lambda which modifies the configuration defaults.

```kotlin
val prefs = KsPrefs(applicationContext) {
    autoSavePolicy = AutoSavePolicy.MANUAL
    commitStrategy = CommitStrategy.COMMIT
}
```

| Field | Type | Description | Default Value |
|-----------------|----------------|--------------------------------------------------------------------------------------------|----------------------|
| encryptionType | EncryptionType | Encryption technique used to cipher and decipher data upon storage operations | PlainText |
| commitStrategy | CommitStrategy | Strategy to use at the moment of writing the preferences into the persistent XML storage | CommitStrategy.APPLY |
| autoSave | AutoSavePolicy | Whether after a `push()` operation changes are saved to the persistent XML storage; strategy according to `commitStrategy` | AutoSavePolicy.AUTO |
| mode | Int | SharedPreferences access mode | Context.MODE_PRIVATE |
| charset | Charset | Charset used for string-to-byte and byte-to-string conversions | Charsets.UTF_8 |
| keyRegex | Regex? | Regular Expression which, if non null, every key must match. | null |

### Read
To retrieve values from the preference storage you can use `pull()`.<br>
Pull comes in 4 functions, 3 of which are unsafe helo.
A function is defined *safe* when you also supply the fallback (Android SharedPreferences calls it `default`) value, so that, for any given key, you always have a concrete value to return.<br>
A function is *unsafe* because there is no guarantee it will return a concrete value, as it only relies on the supplied key to pull the value from the persistent XML storage<br>

```kotlin
val safePull = prefs.pull("username", "nobody")
```

Even though the standard SharedPreferences API always forces you to provide a default (KsPrefs calls it `fallback`) value, KsPrefs lets you leave that out, because supplying an actual instance of an object may get verbose, redundant, and pointless if you are sure the key is present inside the storage, or if for some clever intuition you know that the key holds a value for some time being.

```kotlin
val username = prefs.pull("username")
```

*:pushpin: #1: Using an unsafe version of `pull()` isn't dangerous, as long as you know for sure that the target key contains a value.*<br>
*:pushpin: #2: The other 3 functions accept the type parameter as a kotlin class, as a java class or as a reified generic. For the latter, the bytecode of the function is inlined, in order to allow the generic type to be reified.*<br>

### Write
To save values to the preference storage you can use `push()`<br>
Push takes a key and a value, and stores them inside the preferences.

```kotlin
prefs.push("username", viewModel.username)
```

### Save, Auto Save Policies & Commit Strategies
A pending transaction is a change which is registered in-memory but not on the XML preference file.<br>
To commit any pending transaction to the persistent XML storage, you can use `save()`.

By default, `autoSave` is set to `AutoSavePolicy.AUTO`, and therefore changes are automatically synchronized with the underlying XML file, as with each `push()` call the value is directly committed and saved. Therefore, no pending transaction is created.

However, if `autoSave` is turned off (`AutoSavePolicy.MANUAL`), `push()` will save the change in-memory, but won't write it to the XML file until `save()` is called. This way it will create a pending transaction which will be kept in-memory until a commit operation happens.

Here is a table representing when values are saved, depending on the policy in use.
| `AutoSavePolicy` | AUTO | MANUAL |
|---------|--------------------|--------------------|
| push()  | :white_check_mark: | :x: |
| queue() | :x: | :x: |
| save()  | :white_check_mark: | :white_check_mark: |

*:pushpin: The `AutoSavePolicy` involves when to write changes to the persistent XML storage.*<br>

The best (and default) practise is to use `APPLY`. Then, `COMMIT` is also available, as well as `NONE`, for no-op (Does not write anything, used internally for `queue()`).<br>
`save()` and `push()` use the configuration commit strategy to decide how to save their changes to the persistent XML storage

| `CommitStrategy` | APPLY | COMMIT | NONE |
|--------|--------------------|--------------------|------|
| in-memory | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| XML | :white_check_mark: | :white_check_mark: | :x: |
| async | :white_check_mark: | :x: | :x: |
| atomic | :x: | :white_check_mark: | :x: |

*:pushpin: The `CommitStrategy` involves how to write changes to the persistent XML storage.*<br>

### Queuing
To enqueue values to be written into the preference storage you can use `queue()`. It follows the `push()` syntax.<br>
Not writing the changes to the file makes enqueuing a valid choice for both batch computing or expensive and long-running operations.<br>
`queue()` takes a key and a value, and saves the changes in-memory.<br>
`queue()` does not actually write them to the storage. You can do so by calling `save()` (Or by using `push()` subsequently).
<br><br>
This segment touches a broader concept, which is storing scope. There are two storing scopes for SharedPreferences:
- in-memory (key-value pairs are kept in memory). This is fast to read to/write from, but does not persist application restarts.
- XML (key-value pairs are kept on a file). Writng to a file is mildly expensive but it allows preferences to survive across application restarts.<br><br>
Here is a table explaining how different methods inside KsPrefs touch and go through those storing scopes

| `StoringScope` | in-memory | XML |
|---------|--------------------|---------------------------------|
| push()  | :white_check_mark: | :white_check_mark: (By default) |
| queue() | :white_check_mark: | :x: |
| save()  | :white_check_mark: | :white_check_mark: |

*:pushpin: The `StoringScope` involves at which level changes are propagated.*<br>

In the following snippet (Given that `autoSavePolicy` is set to `AUTO`), `n` in-memory and `x` XML write operations are performed. This, given  `f(n)` and `f(x)` for how long those operations will take, takes `n×f(n) + m×f(x)`. Given that, if using `push()`, `m=n`, then it resolves to `n×(f(n) + f(x))`

```kotlin
for ((index, pic) in picsArray.toList().withIndex()) {
    // Long-running computation
    prefs.push("pic-$index", pic.url)
}
```

Even though this isn't an important speedup, as n (and m) grow, the computation takes longer, also given that `f(n) < f(x)`. Enqueuing values makes `m=1`, and the time/op chart follows a more gentle curve: `n×f(n) + f(x)`.
This improvements dramatically optimizes performances for a large amount of operations.

```kotlin
for ((index, pic) in picsArray.toList().withIndex()) {
    // Long-running computation
    prefs.queue("pic-$index", pic.url)
}

// One save operation
prefs.save()
```

Please note, that if you set `autoSavePolicy` to `MANUAL`, `push()` will only change the in-memory values, and you will need to save them manually anyways.

### Dynamic Delegates
It is really useful and fun to have dynamic properties whose value is a direct representation of what the underlying XML preferences file contains.

```kotlin
val accentColor by prefs.dynamic("accent_color", "#2106F3")
```

When you set a value for this property, it is also updated on the XML preference file, as it is a dynamic reference to the preference.
