package ir.easazade.androidutils

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import ir.easazade.androidutils.classes.Quadruple
import ir.huri.jcal.JalaliCalendar
import timber.log.Timber
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * returns a Jalali date in a string format year/month/day
 */
fun Timestamp.dateHumanReadable(): String = JalaliCalendar(this).run { "$year/$month/$day" }

/**
 * shows android soft keyboard
 */
fun EditText._showKeyboard() {
  Timber.d("showing keyboard")
  post {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
  }
}

/**
 * @return a local format of the number as String
 */
fun Number.localNumbers(): String = try {
  val num = NumberFormat.getInstance().apply { isGroupingUsed = false }.format(this)
  num
} catch (e: Exception) {
  this.toString()
}

/**
 * @return a string format of year/month/day of the timestamp with numbers being in local format
 */
fun Timestamp._formatLocale(): String {
  val calendar = GregorianCalendar()
  calendar.time = this
  val month = (calendar.get(Calendar.MONTH) + 1).localNumbers()
  val day = calendar.get(Calendar.DAY_OF_MONTH).localNumbers()
  val year = calendar.get(Calendar.YEAR).localNumbers()
  return "$year/$month/$day"
}

/***
 * @return a tuple of delta -> days , hours , minutes , seconds
 */
fun Timestamp._getDiff(then: Timestamp): Quadruple<Long, Long, Long, Long> {
  var deltaSeconds = (then.time - this.time) / 1000
  val days = deltaSeconds / (24 * 60 * 60)
  deltaSeconds -= (days * 24 * 60 * 60)
  val hours = deltaSeconds / (60 * 60)
  deltaSeconds -= (hours * 60 * 60)
  val minutes = deltaSeconds / 60
  deltaSeconds -= (minutes * 60)
  val seconds = deltaSeconds
  return Quadruple(days, hours, minutes, seconds)
}

/**
 * @return time delta using _getDiff function
 */
operator fun Timestamp.minus(other: Timestamp) = _getDiff(other)

/**
 * returns a human readable string showing time past from now in farsi like
 * دو روز پیش .
 */
fun Timestamp._timePassedFromNow(): String {
  val (days, hours, minutes, seconds) = this - Timestamp(System.currentTimeMillis())
  return when {
    days > 0 -> String.format("%s روز پیش", days)
    hours > 0 -> String.format("%s ساعت پیش", hours)
    minutes > 0 -> String.format("%s دقیقه پیش", minutes)
    seconds > 0 -> "همین حالا"
    else -> "-"
  }
}

fun AlertDialog._showWithTypeface(font: Typeface) {
  show().apply {
    findViewById<TextView>(android.R.id.message)?.apply {
      typeface = font
    }
    getButton(Dialog.BUTTON_POSITIVE).typeface = font
    getButton(Dialog.BUTTON_NEGATIVE).typeface = font
  }
}

fun Toolbar._setDefaultTypeface(typeface: Typeface) {
  for (i in 0 until childCount) {
    val view = getChildAt(i)
    if (view is TextView) {
      Timber.d("view is textview")
      view.typeface = typeface
    }
  }
}

fun String._nullIfBlank() = if (this.isNotBlank()) this else null

fun String._isValidEmailAddress() = isNotBlank().and(Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun String._isValidUrl() = isNotBlank().and(Patterns.WEB_URL.matcher(this).matches())