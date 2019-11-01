package ir.easazade.androidutils.classes

import com.google.android.material.appbar.AppBarLayout

class AppBarStateChangeListener(
  private val onStateChanged: (appBarLayout: AppBarLayout, state: Int) -> Unit
) : AppBarLayout.OnOffsetChangedListener {

  companion object {
    const val EXPANDED = 101
    const val COLLAPSED = 79
    const val IDLE = 104
  }

  private var mCurrentState = IDLE

  fun getCurrentState(): Int = mCurrentState

  override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
    when {
      i == 0 -> {
        if (mCurrentState != EXPANDED) {
          onStateChanged(appBarLayout, EXPANDED)
        }
        mCurrentState = EXPANDED
      }
      Math.abs(i) >= appBarLayout.totalScrollRange -> {
        if (mCurrentState != COLLAPSED) {
          onStateChanged(appBarLayout, COLLAPSED)
        }
        mCurrentState = COLLAPSED
      }
      else -> {
        if (mCurrentState != IDLE) {
          onStateChanged(appBarLayout, IDLE)
        }
        mCurrentState = IDLE
      }
    }
  }
}