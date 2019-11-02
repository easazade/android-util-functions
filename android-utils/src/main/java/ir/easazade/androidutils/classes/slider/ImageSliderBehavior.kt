package ir.easazade.androidutils.classes.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ir.easazade.androidutils.R
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

class ImageSliderBehavior(
  private val subscriptions: CompositeDisposable,
  private val viewPager: SliderViewPager,
  private val imageUrls: List<String>,
  private val loadImage: (String, ImageView) -> Unit
) : PagerAdapter() {

  private val userTouchingSuscriptions = CompositeDisposable()
  private var userIsTouchingSlider = false

  init {
    viewPager.adapter = this
    viewPager.offscreenPageLimit = 3
    viewPager.clipToPadding = false
    viewPager.setPadding(60, 0, 60, 0)
    viewPager.pageMargin = 15
//    mImagesViewPager.setPageTransformer(true, AlphaPageTransformer())
    viewPager.addTouchListener(this)

    subscriptions.add(
      Observable.interval(4, SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          if (!userIsTouchingSlider)
            viewPager.post {
              if (viewPager.currentItem < imageUrls.size - 1)
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
              else
                viewPager.setCurrentItem(0, true)
            }
        }
    )
  }

  fun setUserIsTouching(touching: Boolean) {
    userTouchingSuscriptions.clear()
    if (touching)
      userIsTouchingSlider = true
    else
      runDelayed(userTouchingSuscriptions, 2000) {
        try {
          userIsTouchingSlider = false
        } catch (e: Exception) {
          Timber.d(e)
        }
      }
  }

  override fun isViewFromObject(view: View, objectt: Any): Boolean = view == objectt as ImageView

  override fun getCount(): Int = imageUrls.size

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val inflater = LayoutInflater.from(container.context)
    val cImageView = inflater.inflate(R.layout.item_plan_slider, container, false) as ImageView
    container.addView(cImageView)
    loadImage(imageUrls[position], cImageView)
    return cImageView
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }

//  fun addTaskToListAndShow(task: Task) {
//    var added = false
//    imageUrls.forEach { item ->
//      if (item.day.date == task.date) {
//        item.tasks.add(task)
//        added = true
//      }
//    }
//    if (!added) {
//
//    }
//  }

  private fun runDelayed(subscription: CompositeDisposable, millis: Long, action: () -> Unit) {
    subscription.add(
      Observable.just(Unit).delay(millis, MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { action.invoke() }
    )
  }
}