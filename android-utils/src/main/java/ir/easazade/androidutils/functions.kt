package ir.easazade.androidutils

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import timber.log.Timber
import java.sql.Timestamp
import java.util.Calendar
import java.util.GregorianCalendar

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
