package ir.easazade.androidutils.classes

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

class HorizontalLinearLayoutManager : LinearLayoutManager {

  constructor(context: Context?) : super(context) {
    orientation = HORIZONTAL
  }

  constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
    context,
    HORIZONTAL,
    reverseLayout
  )

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  ) {

    orientation = HORIZONTAL
  }
}