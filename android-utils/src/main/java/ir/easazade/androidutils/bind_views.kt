package ir.easazade.androidutils

import android.view.View
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A pattern for easier view binding for activities
 */

fun <V : View> bind(id: Int): ReadOnlyProperty<FragmentActivity, V> =
  Lazy { activity: FragmentActivity, prop ->
    activity.findViewById(id) as V?
      ?: throw IllegalStateException("View ID $id for '${prop.name}' not found.")
  }

/**
 * Taken from kotterknife.
 *
 * @see <a href="https://github.com/JakeWharton/kotterknife">JakeWharton/kotterknife</a>
 */
private class Lazy<V>(
  private val initializer: (FragmentActivity, KProperty<*>) -> V
) : ReadOnlyProperty<FragmentActivity, V> {

  private object EMPTY

  private var value: Any? = EMPTY

  override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): V {
    if (value == EMPTY) {
      value = initializer(thisRef, property)
    }
    @Suppress("UNCHECKED_CAST")
    return value as V
  }
}
