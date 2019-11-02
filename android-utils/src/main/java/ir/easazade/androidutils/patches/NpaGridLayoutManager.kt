package ir.easazade.androidutils.patches

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class NpaGridLayoutManager : GridLayoutManager {
  /**
   * Disable predictive animations. There is a bug in RecyclerView which causes views that
   * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
   * adapter size has decreased since the ViewHolder was recycled.
   */
  override fun supportsPredictiveItemAnimations(): Boolean = false

  constructor(context: Context, spanCount: Int) : super(context, spanCount)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  )

  constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(
    context,
    spanCount,
    orientation,
    reverseLayout
  )
}

class NPALinearLayoutManager : LinearLayoutManager {

  /**
   * Disable predictive animations. There is a bug in RecyclerView which causes views that
   * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
   * adapter size has decreased since the ViewHolder was recycled.
   */
  override fun supportsPredictiveItemAnimations(): Boolean = false

  constructor(context: Context?) : super(context)
  constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  )

  override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
    try {
      super.onLayoutChildren(recycler, state)
    } catch (e: IndexOutOfBoundsException) {
      Timber.e("index out of bound exception !!! ")
    }
  }
}