# Welcome to KsPrefs

## Abstract
KsPrefs is a SharedPreferences wrapper for Android applications.
It has a powerful and flexible interface designed to provide unified and simplified primitives to access the Android SharedPreferences system.
It offers a consistent syntax for reading and writing values, whilst parameterizing configurations (Encryption, Mode, Auto Save Policies, Commit Strategies) and enabling fine-grained control over the library's behaviour.


## Design
The library is built with different stages in mind, each taking input from the previous one:

``` mermaid
graph LR
  I[KsPrefs API] --pull() / push()---> D(Dispatcher);
  D --encrypt---> E[Enclosure]
  E --write---> S[Storage];
```

1. `KsPrefs API` allows developers to interface with the main primitives: `pull` and `push`, to read and write values to the storage, respectively;
2. `Dispatcher` takes care of the type mapping and enforces the library specific configurations;
3. `Enclosure` physically encrypts and decrypts the data to/from permanent storage;
4. `Storage` is written to using the actual `SharedPreferences` APIs



## Samples
