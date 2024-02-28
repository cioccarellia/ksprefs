# Dependencies and Integration

### Publishing

All the dependencies are published on MavenCentral. Make sure to have it included in your `reporitories` block in the top level `build.gradle` file.

```groovy
repositories {
    mavenCentral()
}
```

### Project Requirements

- Java 17+ is required;
	```groovy
	compileOptions {
	    sourceCompatibility JavaVersion.VERSION_17
	    targetCompatibility JavaVersion.VERSION_17
	}
	```
- `minSdk` 19+ is required.

### Dependency
???+ gradle "Gradle"

	``` java
	dependencies {
    	implementation "com.github.cioccarellia:ksprefs:2.4.1"
	}
	```

??? gradle "Kotlin DSL"

	``` kotlin
	dependencies {
	    implementation("com.github.cioccarellia:ksprefs:2.4.1")
	}
	```

??? gradle "Maven"

	``` xml
	<dependency>
	    <groupId>com.github.cioccarellia</groupId>
	    <artifactId>ksprefs</artifactId>
	    <version>2.4.1</version>
	    <type>pom</type>
	</dependency>
	```
