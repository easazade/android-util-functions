@file:Suppress("FunctionName")

package ir.easazade.androidutils

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import timber.log.Timber
import java.sql.Timestamp
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.random.Random

/***
 * @return a new instance of pair object just like using Pair(t,n)
 */
fun <N, T> pair(t: T, n: N): Pair<T, N> = Pair(t, n)

/**
 * shows the stacktrace in the logcat using Timber library
 */
fun _showStackTrace() {
  try {
    throw RuntimeException()
  } catch (e: Exception) {
    Timber.w(e)
  }
}

/**
 * @return a Timestamp object with the value of now
 */
fun _currentTime(): Timestamp = Timestamp(System.currentTimeMillis())

/**
 * @return a gregorian date in a string with a format like this -> 2019/2/24 (year/month/day)
 */
fun _getTodayDate(): String {
  val cal = GregorianCalendar()
  cal.time = Timestamp(System.currentTimeMillis())
  return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
}

/**
 * @return a gregorian datetime in a string with a format like this -> 2019-2-24 20:45:00 (year-month-day hour:minute:second)
 */
fun _getTodayDateTime(): String {
  val cal = GregorianCalendar()
  cal.time = Timestamp(System.currentTimeMillis())
  return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)} " +
      "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}"
}

/***
 * launches android telephone activity with the given number dialed
 */
fun _callNumber(activity: FragmentActivity, number: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    activity.startActivity(intent)
  } catch (e: Exception) {
    Timber.e(e)
  }
}

/**
 * @return a random Long number between given min and max
 */
fun _randomNumber(min: Long = 0, max: Long = 100) =
  Random(System.currentTimeMillis()).nextLong(min, max)

/**
 * launches a browser and opens with the given url
 * checks for the url validity first
 */
fun _launchBrowserAndOpenUrl(activity: FragmentActivity, url: String) {
  if (url.isNotEmpty()) {
    if (url.startsWith("www")) {
      val fixUrl = "https://$url"
      val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fixUrl))
      activity.startActivity(browserIntent)
    } else if (url.startsWith("https://") || url.startsWith("http://")) {
      val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      activity.startActivity(browserIntent)
    }
  }
}

/**
 * converts pixels to density pixels
 */
fun _px2dp(px: Float, context: Context): Float {
  return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * converts density pixels to pixels
 */
fun _dp2px(dp: Float, context: Context): Float {
  return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}