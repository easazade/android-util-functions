

# android-util-functions

[![](https://jitpack.io/v/easazade/android-util-functions.svg)](https://jitpack.io/#easazade/android-util-functions)  
***document will be added soon***
  
<p>this is a library containing utility functions (many of them kotlin extension functions) and helper  
classes that I use in my apps. now they are all in one library so that i don't have to search  
older projects and copy paste them one by one</p>

## instal

```groovy
//add this to your project build
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
```kotlin
dependencies {
	implementation 'com.github.easazade:android-util-functions:$last_version'
}
```

## Docs

| functions |description  |
|--|--|
| _convertImplicitIntentToExplicit() | .. |
|_openSettings()|opens app settings|
|_askToTurnOnLocationIfOff()|shows a dialog to user asking him to grant access to device location|
|_getAppVersion()|returns app version|
|_showSnackBarMessage()|helper function to show a customized snackbar message|
|_isConnected()|checks for device's connectivity|
|bind()|A pattern for easier view binding for activities with lazy instantiation example : `val image:ImageView by bind(id)`|
