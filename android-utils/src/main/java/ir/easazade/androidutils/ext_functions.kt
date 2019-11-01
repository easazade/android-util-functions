package ir.easazade.androidutils

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ir.huri.jcal.JalaliCalendar
import timber.log.Timber
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * returns a Jalali date in a string format
 */
fun Timestamp._toJalaliHumanReadableDate(): String {
  val jalaliCalendar = JalaliCalendar(this)
  return "${jalaliCalendar.year}/${jalaliCalendar.month}/${jalaliCalendar.day}"
}

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

fun AlertDialog._showWithTypeface(font: Typeface) {
  show().apply {
    findViewById<TextView>(android.R.id.message)?.apply {
      typeface = font
    }
    getButton(Dialog.BUTTON_POSITIVE).typeface = font
    getButton(Dialog.BUTTON_NEGATIVE).typeface = font
  }
}

fun String._nullIfBlank() = if (this.isNotBlank()) this else null

fun String._isValidEmailAddress() = isNotBlank().and(Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun String._isValidUrl() = isNotBlank().and(Patterns.WEB_URL.matcher(this).matches())