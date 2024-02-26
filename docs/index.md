# Welcome to KsPrefs

## Abstract
KsPrefs is a SharedPreferences wrapper for Android applications.
It has a powerful and flexible interface designed to provide unified and simplified primitives to access the Android SharedPreferences system. It offers a consistent syntax for reading and writing values, whilst parameterizing configurations (Auto save policies and Commit strategies) and enabling fine-grained control over the system's behaviour.





## Motivation & Use Cases

The motivation behind KsPrefs stems from the need for a SharedPreferences solution that not only enhances security through cryptography but also streamlines the development process with a clean and intuitive API. Whether you're a seasoned developer or just starting, KsPrefs provides a hassle-free experience for managing shared preferences.

## Philosophy

**Common Access Interface:**
KsPrefs adheres to the philosophy of establishing a common access interface for the SharedPreferences system. This means you can interact with SharedPreferences using the same syntax regardless of the chosen encryption standard, allowing for a seamless development experience.

**Customization and Parameterization:**
Beyond its simplicity, KsPrefs offers extensive customization options. You have the flexibility to parameterize the encryption standard, save policy, and commit strategy, tailoring the library's behavior to suit the specific requirements of your project.

## Technical Material

To dive deeper into the technical aspects of KsPrefs, explore the comprehensive set of features, including customizable behavior, encryption options, and extensive support for different data types and enums.

---






## Design
Each kevlar package contains custom implementations for what it has to scan for, but they all share the same overall structure, to make it easy to work with. Once you learn how to use a package, then you can transfer that knowledge to the other ones.

``` mermaid
graph LR
  I[Inizialization] -.Settings..-> K{Kevlar};
  AREQ[Attestation Requests] --> K
  K --> ARES[Attestation Result]
  ARES --> |Clear| P[Passed];
  ARES --> |Failed| NP[Not Passed];
```


