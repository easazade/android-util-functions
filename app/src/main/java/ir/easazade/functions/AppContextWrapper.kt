package ir.easazade.functions

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class AppContextWrapper {

  companion object {
    fun wrap(base: Context?): Context? {
      var context: Context? = base
      //this is the source
      //https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758
      //setFab fixed locale
      //since this app has only one language
      val locale = Locale("fa")
      Locale.setDefault(locale) //not sure about this line
      val config = Configuration()
      when {
        (Build.VERSION.SDK_INT >= 17) -> {
          config.setLocale(locale)
          context = context?.createConfigurationContext(config)
        }
        else -> {
          config.locale = locale
          context?.resources?.updateConfiguration(config, context?.resources?.displayMetrics)
        }
      }
      return context
    }
  }
}