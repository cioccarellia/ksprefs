# Welcome to KsPrefs

## Abstract
KsPrefs is a SharedPreferences wrapper for Android applications.
It has a powerful and flexible interface designed to provide unified and simplified primitives to access the Android SharedPreferences system. It offers a consistent syntax for reading and writing values, whilst parameterizing configurations (Auto save policies and Commit strategies) and enabling fine-grained control over the system's behaviour.






## Design
``` mermaid
graph LR
  I[Inizialization] -.Settings..-> K{k};
  AREQ[Attestation Requests] --> K
  K --> ARES[Attestation Result]
  ARES --> |Clear| P[Passed];
  ARES --> |Failed| NP[Not Passed];
```


