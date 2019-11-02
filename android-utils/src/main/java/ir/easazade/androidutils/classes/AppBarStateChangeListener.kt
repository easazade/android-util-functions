package ir.easazade.androidutils.classes

import com.google.android.material.appbar.AppBarLayout
import ir.easazade.androidutils.classes.AppBarStateChangeListener.State.COLLAPSED
import ir.easazade.androidutils.classes.AppBarStateChangeListener.State.EXPANDED
import ir.easazade.androidutils.classes.AppBarStateChangeListener.State.IDLE
import kotlin.math.abs

/**
 * this class
 */
class AppBarStateChangeListener(
  private val onStateChanged: (appBarLayout: AppBarLayout, state: State) -> Unit
) : AppBarLayout.OnOffsetChangedListener {

  enum class State {
    EXPANDED, COLLAPSED, IDLE
  }

  var currentState: State = IDLE
    private set

  override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
    when {
      i == 0 -> {
        if (currentState != EXPANDED) {
          onStateChanged(appBarLayout, EXPANDED)
        }
        currentState = EXPANDED
      }
      abs(i) >= appBarLayout.totalScrollRange -> {
        if (currentState != COLLAPSED) {
          onStateChanged(appBarLayout, COLLAPSED)
        }
        currentState = COLLAPSED
      }
      else -> {
        if (currentState != IDLE) {
          onStateChanged(appBarLayout, IDLE)
        }
        currentState = IDLE
      }
    }
  }
}