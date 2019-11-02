package ir.easazade.androidutils.classes.slider

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SliderViewPager : ViewPager {

  private var slider: ImageSliderBehavior? = null

  fun addTouchListener(slider: ImageSliderBehavior) {
    this.slider = slider
  }

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  override fun onTouchEvent(ev: MotionEvent?): Boolean {
    when (ev?.action) {
      MotionEvent.ACTION_DOWN -> slider?.setUserIsTouching(true)
      MotionEvent.ACTION_UP -> slider?.setUserIsTouching(false)
    }

    return super.onTouchEvent(ev)
  }
}