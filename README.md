# android-util-functions  
  
[![](https://jitpack.io/v/easazade/android-util-functions.svg)](https://jitpack.io/#easazade/android-util-functions)  
  
<p>this is a library containing utility functions (many of them kotlin extension functions) and helper  
classes that I use in my apps to remove boilerplate code and make application code more concise and readable</p>  
<p> now they are all in one library so that i don't have to search  older projects and copy paste them one by one</p>  
  
## install  
  
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
  
| functions | Description  |
|--|--|  
|`bind(..)`|A pattern for easier view binding for activities with lazy instantiation example  `val image:ImageView by bind(id)`|  
|`_convertImplicitIntentToExplicit(..)` | .. |  
|`_openSettings(..)`|opens app settings|  
|`_askToTurnOnLocationIfOff(..)`|shows a dialog to user asking him to grant access to device location|  
|`_getAppVersion(..)`|returns app version|  
|`_showSnackBarMessage(..)`|helper function to show a customized snackbar message|  
|`_isConnected(..)`|checks for device's connectivity|
|`pair(t,n)`|return a new instance of pair object just like using `Pair(t,n)`|
|`_showStackTrace()`|shows the stacktrace in the logcat using Timber library|
|`_currentTime()`|return a Timestamp object with the value of now|
|`_getTodayDate()`|return a gregorian date in a string with a format like this -> 2019/2/24 (year/month/day)|
|`_getTodayDateTime()`|return a gregorian datetime in a string with a format like this -> 2019-2-24 20:45:00 (year-month-day hour:minute:second)|
|`_callNumber(activity, number)`|launches android telephone activity with the given number dialed|
|`_randomNumber(min, max)`|return a random Long number between given min and max|
|`_launchBrowserAndOpenUrl(activity, url)`|launches a browser and opens with the given url checks for the url validity first|
|`_px2dp(px,context)`|converts px to dp|
|`_dp2px(dp,context)`|converts dp to px|

<br>
<br>

| extension functions | Description  |
|--|--|  
|`Timestamp.dateHumanReadable()`|returns a Jalali date in a string format `year/month/day`|
|`EditText._showKeyboard()`|shows android soft keyboard|
|`Number.localNumbers()`|return a local format of the number as String|
|`Timestamp._dateHumanReadableformatLocale()`|return a string format of `year/month/day` of the timestamp with numbers being in local format|
|`Timestamp._getDiff(..)`|return a tuple of delta -> days , hours , minutes , seconds|
|`Timestamp._timePassedFromNow()`|returns a human readable string showing time past from now in farsi like  `دو روز پیش`|
|`FragmentActivity._hideKeyboard()`|hides keyboard|
|`AlertDialog._showWithTypeface(..)`|-|
|`Toolbar._setDefaultTypeface(..)`|-|
|`String._nullIfBlank()`|-|
|`String._isValidEmailAddress()`|-|
|`String._isValidUrl()`|-|

<br>
<br>

| List utility functions | Description  |
|--|--|  
|`MutableList<T>._getPagination(page, count)`|returns the page items of the given page based on the page number and count of items in each page|
|`_compareLists(..)`|returns a pair of lists that each contains the different items each provided list argument have   if there is no difference both returned list in Pair object will be empty|
|`MutableList<T>._addAllIfNotExists(..)`|add items to the list from new items if they do not match the predicate with any items in list return list of indexes from new items list that were added to the list|
|`MutableList<T>._addOrUpdateAll()`|add items to the list from newItems if they do not with any items in list (don't already exists) and if they exists they will be replaced by the newer version from newItems list  return list of indexes from newItems list that were added to the list or replaced another item in list|
|`MutableList<T>._contains(item,predicate)`|checks if any item in the list matches our parameter item using predicate to find a match|

# classes

**ForceFarsiLocaleContextWrapper**
<p>a context-wrapper to override the operating system locale and make app to use the locale we want</p>

```kotlin
abstract class BaseActivity : AppCompatActivity() { 

  override fun attachBaseContext(newBase: Context?) {  
    super.attachBaseContext(ForceFarsiLocaleContextWrapper.wrap(newBase))  
  }  
  //rest of the code
}
```
<br>

**AppBarStateChangeListener**
<p>this is a callback class supporting all 3 states of AppBarLayout which are EXPANDED, COLLAPSED, IDLE</p>

```kotlin
appBarLayout.addOnOffsetChangedListener(AppBarStateChangeListener { appBar, state ->  
  when (state) {  
    COLLAPSED -> {} 
    IDLE -> {}
    EXPANDED -> {}
  }  
})
```
```kotlin
//can always get current state of appBarLayout if we have a reference to the listener

val listener:AppBarStateChangeListener = ...
appBarLayout.addOnOffsetChangedListener(listener)
//some code ...

val state = listener.currentState
```
<br>

**Quadruple**
<p>Represents a generic tuple of four values</p>

```kotlin
val tuple = Quadruple(request,status,response,null)
```