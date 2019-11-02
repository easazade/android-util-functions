@file:Suppress("FunctionName")

package ir.easazade.androidutils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import ir.easazade.androidutils.classes.slider.ImageSliderBehavior
import ir.easazade.androidutils.classes.slider.SliderViewPager
import timber.log.Timber

fun _convertImplicitIntentToExplicitIntent(pm: PackageManager, implicitIntent: Intent): Intent? {
  val resolveInfoList = pm.queryIntentServices(implicitIntent, 0)
  if (resolveInfoList == null || resolveInfoList.size != 1) {
    return null
  }
  val serviceInfo = resolveInfoList[0]
  val component = ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
  val explicitIntent = Intent(implicitIntent)
  explicitIntent.component = component
  return explicitIntent
}

/**
 * opens settings page
 */
fun _openSettings(activity: FragmentActivity) {
  val intent = Intent()
  intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
  val uri = Uri.fromParts("package", activity.packageName, null)
  intent.data = uri
  activity.startActivity(intent)
}

/**
 * launches baazar app market commenting interface
 */
fun _commentOnBazar(activity: FragmentActivity) {
  val bazzarPackageName = "com.farsitel.bazaar"
  val appPackageName = activity.application.packageName
  if (appPackageName != null) {
    try {
      val intent = Intent(Intent.ACTION_EDIT)
      intent.data = Uri.parse("bazaar://details?id=$appPackageName")
      intent.setPackage(bazzarPackageName)
      activity.startActivity(intent)
    } catch (e: Exception) {
      Timber.e(e)
    }
  }
}

/**
 * launches application page on baazar market
 */
fun _goToAppPageOnBazar(activity: FragmentActivity) {
  try {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("bazaar://details?id=${activity.packageName}")
    intent.setPackage("com.farsitel.bazaar")
    activity.startActivity(intent)
  } catch (e: Exception) {
    Timber.e(e)
  }
}

/**
 * shows a dialog to user asking him to grant access to device location
 */
fun _askToTurnOnLocationIfOff(activity: FragmentActivity, action: () -> Unit) {
  val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
    // Build the alert dialog
    val builder = AlertDialog.Builder(activity)
    val title = activity.getString(R.string.location_not_active)
    val message = activity.getString(R.string.enable_location_high_accuracy)

    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialogInterface, i ->
      // Show location settings when the user acknowledges the alert dialog
      val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
      activity.startActivity(intent)
    }
    val alertDialog = builder.create()
    alertDialog.setCanceledOnTouchOutside(false)
    alertDialog.show()
  } else {
    action()
  }
}

/**
 * @return application version
 */
fun _getAppVersion(context: Context): String {
  var version = "1.0.0"
  try {
    val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    version = pInfo.versionName
  } catch (e: Exception) {
    Timber.e(e)
  }
  return version
}

/**
 * utility function to show a customized snackbar message
 */
fun _showSnackBarMessage(
  activity: FragmentActivity,
  msg: String,
  rootViewId: Int,
  backgroundColor: Int,
  textColorId: Int,
  actionButtonTextColorId: Int? = Color.BLACK,
  durationMillis: Int? = null,
  actionName: String? = null,
  action: (() -> Unit)? = null,
  showIndefinite: Boolean = false,
  typeface: Typeface
): Snackbar? {
  if (msg.isNotBlank()) {
    val root: View = activity.findViewById(rootViewId)
    val length = if (showIndefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_SHORT
    val snackbar = Snackbar.make(root, msg, length)

    if (durationMillis != null && !showIndefinite) {
      snackbar.duration = durationMillis
    }
    if (action != null && actionName != null) {
      snackbar.setAction(actionName) {
        action.invoke()
      }
      actionButtonTextColorId?.let { colorId ->
        snackbar.setActionTextColor(ContextCompat.getColor(activity, colorId))
        val actionTV =
          snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        actionTV?.let { textView ->
          textView.typeface = typeface
        }
      }
    }
    snackbar.view.setBackgroundColor(ContextCompat.getColor(activity, backgroundColor))
    val snackText =
      snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    snackText.typeface = typeface
    snackText.setTextColor(ContextCompat.getColor(activity, textColorId))
    val config = activity.resources.configuration
    if (config != null) {
      if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL)
        snackbar.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }
    snackbar.show()
    return snackbar
  }
  return null
}

/**
 * checks if device is connected
 */
fun _isConnected(context: Context): Boolean {
  val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
}

/**
 * initiates an sliderViewPager
 */
fun SliderViewPager.setImageSlider(
  subscriptions: CompositeDisposable,
  imageUrls: List<String>,
  loadImage: (String, ImageView) -> Unit
) {
  ImageSliderBehavior(subscriptions, this, imageUrls,loadImage)
}
