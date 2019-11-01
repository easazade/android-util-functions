package ir.easazade.androidutils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import ir.huri.jcal.JalaliCalendar
import timber.log.Timber
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

fun <N, T> pair(t: T, n: N): Pair<T, N> = Pair(t, n)

fun Timestamp.toJalaliHumanReadableDate(): String {
  val jalaliCalendar = JalaliCalendar(this)
  return "${jalaliCalendar.year.localNumbers()}/${jalaliCalendar.month.localNumbers()}/${jalaliCalendar.day.localNumbers()}"
}

fun showStackTrace() {
  try {
    throw RuntimeException()
  } catch (e: Exception) {
    Timber.w(e)
  }
}

fun EditText.showKeyboard() {
  Timber.d("showing keyboard")
  post {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
  }
}

fun Number.localNumbers(): String = try {
  val num = NumberFormat.getInstance().apply { isGroupingUsed = false }.format(this)
  num
} catch (e: Exception) {
  this.toString()
}

fun Timestamp.extFormatLocale(): String {
  val calendar = GregorianCalendar()
  calendar.time = this
  val month = (calendar.get(Calendar.MONTH) + 1).localNumbers()
  val day = calendar.get(Calendar.DAY_OF_MONTH).localNumbers()
  val year = calendar.get(Calendar.YEAR).localNumbers()
  return "$year/$month/$day"
}

fun AlertDialog.extShowWithTypeface(font: Typeface) {
  show().apply {
    findViewById<TextView>(android.R.id.message)?.apply {
      typeface = font
    }
    getButton(Dialog.BUTTON_POSITIVE).typeface = font
    getButton(Dialog.BUTTON_NEGATIVE).typeface = font
  }
}
//
//fun runDelayed(subscription: CompositeDisposable, millis: Long, action: () -> Unit) {
//  subscription.add(
//    Observable.just(Unit).delay(millis, MILLISECONDS)
//      .subscribeOn(App.component().threads().ioScheduler())
//      .observeOn(App.component().threads().uiScheduler())
//      .subscribe { action.invoke() }
//  )
//}

fun String.nullIfBlank(): String? = if (this.isNotBlank()) this else null

fun currentTime(): Timestamp = Timestamp(System.currentTimeMillis())

fun getTodayDate(): String {
  val cal = GregorianCalendar()
  cal.time = Timestamp(System.currentTimeMillis())
  return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)}"
}

fun getTodayDateTime(): String {
  val cal = GregorianCalendar()
  cal.time = Timestamp(System.currentTimeMillis())
  return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DAY_OF_MONTH)} " +
      "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}"
}

fun String.isValidEmailAddress(): Boolean =
  isNotBlank().and(Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun String.isValidUrl(): Boolean =
  isNotBlank().and(Patterns.WEB_URL.matcher(this).matches())

fun printStackTrace() {
  try {
    throw RuntimeException()
  } catch (e: Exception) {
    Timber.d(e)
  }
}

fun callNumber(activity: FragmentActivity, number: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    activity.startActivity(intent)
  } catch (e: Exception) {
    Timber.e(e)
  }
}
