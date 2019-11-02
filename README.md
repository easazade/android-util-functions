


# android-util-functions

[![](https://jitpack.io/v/easazade/android-util-functions.svg)](https://jitpack.io/#easazade/android-util-functions)

<p>this is a library containing utility functions (many of them kotlin extension functions) and helper
classes that I use in my apps to remove boilerplate code and make application code more concise and readable</p>
<p> now they are all in one library so that i don't have to search  older projects and copy paste them one by one</p>

## instal

```groovy
//add this to your project build.gradle file
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
```
```kotlin
//add this to your app build.gradle file
dependencies {
   implementation 'com.github.easazade:android-util-functions:$last_version'
}
```

## Docs
***complete document for rest of the functions will be added soon***

| functions |description  |
|--|--|
|`bind(..)`|A pattern for easier view binding for activities with lazy instantiation example  `val image:ImageView by bind(id)`|
|`_convertImplicitIntentToExplicit(..)` | .. |
|`_openSettings(..)`|opens app settings|
|`_askToTurnOnLocationIfOff(..)`|shows a dialog to user asking him to grant access to device location|
|`_getAppVersion(..)`|returns app version|
|`_showSnackBarMessage(..)`|helper function to show a customized snackbar message|
|`_isConnected(..)`|checks for device's connectivity|