# Celestial

This is a Kotlin Multiplatform project targeting Android, iOS. It connects to the NASA api (see getting started)
and displays the nasa image of the day, mars rover images, and more. 

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Getting started
Generate an api key from (the NASA api)[api.nasa.gov], create a file at the root directory named `secret.properties`
and add the value `NASA_API_KEY=YOU-API-KEY`, then just run the app from Android Studio or Xcode, that's it!


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…