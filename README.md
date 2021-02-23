<p align="center">
  <a href="https://github.com/cioccarellia/ksprefs" target="_blank"><img width="100" src="extras/ksprefs.png"></a>
</p>
<h1 align="center">KsPrefs</h1>
<p align="center">SharedPreferences. 100% Kotlin.</p>
<p align="center">
  <a href="https://search.maven.org/artifact/com.github.cioccarellia/ksprefs"><img src="https://img.shields.io/maven-central/v/com.github.cioccarellia/ksprefs.svg?label=Maven%20Central" alt="Download from MavenCentral"></a>
  <a href="https://app.circleci.com/pipelines/github/cioccarellia/ksprefs"><img src="https://circleci.com/gh/cioccarellia/ksprefs.svg?style=svg" alt="CircleCI"></a>
  <a href="https://app.codacy.com/manual/cioccarellia/ksprefs/dashboard"><img src="https://api.codacy.com/project/badge/Grade/f10cdbdbe7b84d0ea7a03b755c104e03" alt="Codacy"></a>
  <a href="kotlin versions maven"><img src="https://img.shields.io/badge/kotlin-1.4.30-orange.svg" alt="Kotlin"></a>
  <a href="https://source.android.com/setup/start/build-numbers"><img src="https://img.shields.io/badge/min-19-00e676.svg" alt="Android Min Sdk"></a>
  <a href="https://source.android.com/setup/start/build-numbers"><img src="https://img.shields.io/badge/compile-30-00e676.svg" alt="Android Compile Version"></a>
  <a href="https://github.com/cioccarellia/ksprefs/blob/master/LICENSE.md"><img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg" alt="License"></a>
</p>


#### ⚠️ since 2.2.4 ksprefs has been moved to mavenCentral. The publishing coordinates have changed.
<details open><summary>Gradle</summary>

```gradle
dependencies {
    implementation 'com.github.cioccarellia:ksprefs:2.2.4'
}
```
</details>

<details><summary>Maven</summary>

```xml
<dependency>
    <groupId>com.github.cioccarellia</groupId>
    <artifactId>ksprefs</artifactId>
    <version>2.2.4</version>
    <type>pom</type>
</dependency>
```
</details>

- ⚡️ Powerful SharedPreferences wrapper & API.
- 🚀 Easy to pick up & use right away.
- ⚙️ Fully customizable behaviour.
- 🔒 Built-in cryptography & decoding engines (PlainText, Base64, AES-CBC, AES-ECB, Android KeyStore + AES-GCM / RSA KeyPair).
- 🗂 Extensive type & enum support.
- 🧡 Kotlin powered.

```kotlin
val prefs = KsPrefs(applicationContext)
```

To _read_ from SharedPreferences, use `pull(key, fallback)`.<br>
To _write_ to SharedPreferences, use `push(key, value)`.

## Introduction
<img src="extras/dark/png/scheme.png"><br><br>
KsPrefs (<b>K</b>otlin <b>S</b>hared <b>Pref</b>erences) is a wrapper for the default Android SharedPreferences (_SP_ for short) implementation.
Its goal is to bring security to preference storage through cryptography. This library can be used as a replacement of SP whick lacks security and practicality, and which even Google is moving away from with [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore).<br>
On top of the SP API, KsPrefs extends with numerous features and extra bits which com pre-packed with the library, and can be used to enhance the development experience.

## Functionality
### Constructor
You should create `KsPrefs` only once among your codebase. 
```kotlin
val prefs = KsPrefs(applicationContext)
```

It is recommended to keep it inside your `Application` class, so that it's accessible anywhere in your code.

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
KsPrefs heavily depends on its configuration.<br>
To customize KsPrefs, you can pass in a lambda which modifies the configuration defaults values.

```kotlin
val prefs = KsPrefs(applicationContext) {
	encryptionType = PlainText()
    autoSavePolicy = AutoSavePolicy.MANUAL
    commitStrategy = CommitStrategy.COMMIT
}
```

| Field | Type | Description | Default Value |
|-----------------|----------------|--------------------------------------------------------------------------------------------|----------------------|
| encryptionType | EncryptionType | Encryption technique used to cipher and decipher data upon storage operations | PlainText() |
| commitStrategy | CommitStrategy | Strategy to use at the moment of writing the preferences into the persistent XML storage | CommitStrategy.APPLY |
| autoSave | AutoSavePolicy | Whether after a `push()` operation changes are saved to the persistent XML storage; saving strategy depending on `commitStrategy` | AutoSavePolicy.AUTOMATIC |
| mode | Int | SharedPreferences access mode | Context.MODE_PRIVATE |
| charset | Charset | Charset used for string-to-byte and byte-to-string conversions | Charsets.UTF_8 |
| keyRegex | Regex? | Regular Expression which, if non null, every key must match. | null |

### Read
To retrieve values from the preference storage you use `pull()`.<br>
There are 4 different functions named `pull`, 3 of which are unsafe. <!-- helo -->
A function is defined *safe* when you supply the fallback (Android SharedPreferences defines it `default`) value, so that, for *any* given key, you always have a concrete in-memory value to return.<br>
A function is *unsafe* because there is no guarantee it will return a concrete value, as it only relies on the supplied key to pull the value from the persistent XML storage<br>

```kotlin
val safePull = prefs.pull("username", "nobody")
```

Even though the standard SharedPreferences API always forces you to provide a default (KsPrefs defines it `fallback`) value, KsPrefs allows you to leave that out, because supplying an actual instance of an object, in some contexts is verbose and redundant if you are know that the key is present inside the persistent storage, or if for some clever intuition you know that the key holds a value at some specific time.

```kotlin
val username = prefs.pull("username")
```

*:pushpin: #1: Using an unsafe version of `pull()` isn't dangerous, as long as you are sure the target key holds a value.*<br>
*:pushpin: #2: The 3 unsafe functions accept the type parameter as a kotlin class, as a java class or as a kotlin generic. For the latter, the bytecode of the function is inlined, in order for the generic type to be reified.*<br>

### Write
To save values to the preference storage you use `push()`<br>
Push takes a key and a value, and stores them inside the preferences.

```kotlin
prefs.push("username", viewModel.username)
```

### Saving, Auto Save Policies & Commit Strategies
A pending transaction is a change which is registered in-memory, but not yet on the XML preference file.
Android SharedPreferences works that way; indeed, you can stack up pending transactions, but at some point you have to _actually_ save them.
If out app were to shut down unexpectedly, those changes would be lost forever. <br>
To commit any pending transaction to the persistent XML storage, in ksprefs you use `save()`. 
This matches `commit()` and `apply()` SharedPreferences behaviour you may be accustomed to.<br>

#### Auto Save Policy
By default, `autoSave` is set to `AutoSavePolicy.AUTOMATIC`, and therefore changes are automatically synchronized with the underlying XML file, because after each `push()` call, a `save()` follows, in order to automatically commit and save the preference. Therefore, no pending transaction is kept.

However, if `autoSave` is turned off (using `AutoSavePolicy.MANUAL`), `push()` will save the change in-memory, but is not going to write it to the XML preferences file until `save()` is invoked. This way it's going to create a pending transaction which will be kept in-memory until a `save()` operation happens.

Here is a table representing when values are saved to the storage, depending on the policy in use.

| `AutoSavePolicy` | AUTO | MANUAL |
|---------|--------------------|--------------------|
| push()  | :white_check_mark: | :x: |
| queue() | :x: | :x: |
| save()  | :white_check_mark: | :white_check_mark: |
| SharedPreferences.Editor.commit()  | :white_check_mark: | :white_check_mark: |
| SharedPreferences.Editor.apply()  | :white_check_mark: | :white_check_mark: |

*:pushpin: `AutoSavePolicy` chooses when to write changes to the persistent XML storage and when to keep them in memory.*<br>

#### Commit Strategy
The best (and default) practise while dealing with SharedPreferences is to use `APPLY`. It is asynchronous and fast. `COMMIT` is also available, though it should not be used unless you have a valid reason to, given its synchronous and strict nature, as well as `NONE`, for no-op (Does not save anything, used internally for `queue()`).<br>
`save()` and `push()` always refer to the commit strategy to decide how to save their updates to the persistent XML preference storage.

Here is a table representing various features of different commit strategies. Check out the official documentation [here](https://developer.android.com/reference/android/content/SharedPreferences.Editor.html) and see [this](https://stackoverflow.com/questions/5960678/whats-the-difference-between-commit-and-apply-in-sharedpreferences) post for more intel.

| `CommitStrategy` | APPLY | COMMIT | NONE |
|--------|--------------------|--------------------|------|
| in-memory | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| XML | :white_check_mark: | :white_check_mark: | :x: |
| async | :white_check_mark: | :x: | :heavy_minus_sign:	 |
| atomic | :white_check_mark: | :white_check_mark: | :heavy_minus_sign:	 |
| error report | :x: | :white_check_mark: | :heavy_minus_sign:	 |

*:pushpin: The `CommitStrategy` involves how to write changes to the persistent XML storage.*<br>

### Queuing
To enqueue values to be written into the preference storage you use `queue()`. It follows `push()`'s syntax.<br>
While `push`, by default, _pushes_ the update immediately on the XML persistent storage (By default, changable with `AutoSave`), `queue()` saves the update in-memory without writing it out to the storage.<br>
Not writing the changes to the file makes enqueuing a valid choice for both batch computing or resource-expensive and long-running operations.<br>
- `queue()` takes a key and a value, and saves the changes in-memory.<br>
- `queue()` does not actually send updates to the storage. You can do so by calling `save()` (Or by using `push()` subsequently).
<br><br>

This segment touches a broader concept, which is storing scope.
There are two storing scopes for SharedPreferences:
- in-memory (key-value pairs are kept in memory). This is fast to read to/write from, but does not persist application restarts.
- XML (key-value pairs are kept on a file). Writing to a file is mildly expensive but it allows preferences to survive across application restarts.<br>
Here is a table explaining how different methods inside KsPrefs touch and go through those storing scopes.

| `StoringScope` | in-memory | XML |
|---------|--------------------|---------------------------------|
| push(k, v)  | :white_check_mark: | :white_check_mark: (By default) |
| queue(k, v) | :white_check_mark: | :x: |
| save()      | :white_check_mark: | :white_check_mark: |

*:pushpin: The `StoringScope` determines at which level changes are propagated.*<br>

In the following snippet (Given that `autoSavePolicy` is set to `AUTOMATIC`), `n` in-memory and `x` XML write operations are performed. This, given  `f(n)` and `f(x)` for how long those operations will take, takes `n×f(n) + m×f(x)`. Given that, if using `push()`, `m=n`, then it resolves to `n×(f(n) + f(x))`

```kotlin
for ((index, pic) in picsArray.toList().withIndex()) {
    // Long-running computation
    prefs.push("pic-$index", pic.url)
}
```

Even though this isn't a significant speedup for small data sizes, as n (and m) grow the computation takes longer; since enqueuing values sets `m=1`, thus, `f(n) < f(x)`. The time/op chart follows a much more gentle curve: `n×f(n) + f(x)`.
This improvements drastically optimizes performances for a large amount of operations.

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

## API
KsPrefs provides some customizable data structures, to abstract preference reads/writes to function calls.

### Preferences Center
A `PrefsCenter` is an extremely simple and straightforward class. It is used to enclose and contain all the SharedPreferences-specific operations, like providing a key, doing some value specific post-read/pre-write operation, providing the fallback value or the return type.

```kotlin
object StartCounterPrefCenter : PrefsCenter(App.prefs) {
    private const val counterKey = "start_counter"
    
    fun increment() = prefs.push(counterKey,  read() + 1)
    fun read() = prefs.pull(counterKey, 0)
}
```

## Sample App
<img src="https://raw.githubusercontent.com/cioccarellia/ksprefs/master/art/demo-app.png">
