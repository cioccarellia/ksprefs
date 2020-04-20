# Library Template (ANDROID)

This repository can be used as a template to create new GitHub repositories for Kotlin/Android libraries.

### Code Formatting

```gradle
./gradlew spotlessApply
```

*Make sure you update [spotless.license.kt](spotless.license.kt) and [LICENSE.md](LICENSE.md) to reflect your own license and author info!* Other settings for this plugin can be tweaked in [gradle/spotless_plugin_config.gradle](gradle/spotless_plugin_config.gradle).

### Check if Dependencies Are Up-to-Date

```gradle
./gradlew dependencyUpdates
```

Settings can be tweaked for this plugin within [gradle/versions_plugin_config.gradle](gradle/versions_plugin_config.gradle).

### Publishing the Library to Bintray -> jCenter

This repository is setup to automatically publish to Bintray when you create a new release from
GitHub.

But there are some pre-requisites:

1. Update [library_info.gradle](library_info.gradle) to contain your library's information (group, description, etc.).
2. From your GitHub repository settings, add **secrets** for `BINTRAY_ORG`, `BINTRAY_USER`, and `BINTRAY_API_KEY`. 
These will be used by the `Bintray Release` GitHub Actions workflow. 
3. With each release, update the version name and code in [library_info.gradle](library_info.gradle#L6-L8).
4. Once you've deployed to Bintray for the first time, you can link your Bintray repository to jCenter so 
people can depend on your library from Android projects _without_ having to add any special Maven repositories.

How does releasing work?

1. **Automatically** when you create a [Release](https://help.github.com/en/github/administering-a-repository/releasing-projects-on-github) 
in your GitHub repository.
2. OR if you set the three environment variables mentioned above, and invoke `./gradlew bintrayUpload`.
