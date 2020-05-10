<p align="center">
  <a href="https://github.com/cioccarellia/ksprefs" target="_blank"><img width="100" src="extras/ksprefs.png"></a>
</p>
<h1 align="center">KsPrefs</h1>
<p align="center">Simplify SharedPreferences. 100% Kotlin.</p>
<p align="center">
  <a tagret="_blank" href="https://bintray.com/cioccarellia/kurl/kurl/_latestVersion"><img src="https://api.bintray.com/packages/cioccarellia/maven/ksprefs/images/download.svg" alt="Download from Bintray"></a>
  <a tagret="_blank" href="https://app.circleci.com/pipelines/github/cioccarellia/ksprefs"><img src="https://circleci.com/gh/cioccarellia/ksprefs.svg?style=svg" alt="CircleCI"></a>
  <a tagret="_blank" href="https://app.codacy.com/manual/cioccarellia/ksprefs/dashboard"><img src="https://api.codacy.com/project/badge/Grade/e5d15b52c1884f1792667d9364352c67" alt="Codacy"></a>
  <a><img src="https://img.shields.io/badge/kotlin-1.3.72-orange.svg" alt="Kotlin"></a>
  <a><img src="https://img.shields.io/badge/min-19-00e676.svg" alt="Android Min Sdk"></a>
  <a><img src="https://img.shields.io/badge/compile-29-00e676.svg" alt="Android Compile Version"></a>
  <a href="https://github.com/cioccarellia/ksprefs/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg" alt="License"></a>
</p>

### TLDR
```gradle
implementation 'com.cioccarellia.ksprefs:$version'
```

- :zap: Fast SharedPreferences wrapper
- :rocket: Easy to pick up & use right away for any project
- :gear: Configurable behaviour
- :lock: Built-in transformations (PlainText, Base64, AES, Keystore)
- :symbols: Extensive type support
- :heart: Kotlin powered. Standard.

```kotlin
val prefs = KsPrefs(applicationContext)
```

To read from SharedPreferences, you can use `pull(key, default)`.<br>
To write to SharedPreferences, you can use `push(key, value)`.

## Introduction
<img src="extras/light/png/scheme.png"><br>
KsPrefs (<b>K</b>otlin <b>S</b>hared <b>Pref</b>erences) is a wrapper for the default Android SharedPreferences implementation.
It works by providing control and extensibility to the default API, to create strongly-typed, null safe.
KsPrefs
