package ir.easazade.androidutils.classes

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

//TODO add docs to README.md
class EndlessRecyclerViewScrollListener(
  private val layoutManager: RecyclerView.LayoutManager,
  private var visibleThreshold: Int,
  private val onLoadMore: (page: Int) -> Unit,
  private val isShowingLoadMore: () -> Boolean,
  private val isShowingSkeletonItems: () -> Boolean
) : RecyclerView.OnScrollListener() {

  private var currentPage = 0
  private var previousTotalItemCount = 0
  var loadingMore = true
  private val startingPageIndex = 0

  init {
    if (layoutManager is StaggeredGridLayoutManager) {
      visibleThreshold *= layoutManager.spanCount
    } else if (layoutManager is GridLayoutManager) {
      visibleThreshold *= layoutManager.spanCount
    }
  }

  override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
    /**
     * isShowingLoadMore()
     * the purpose of this function is to fix the mistake our first if statement
     * because our first if always misscalculates that wh
     */
    val lastVisibleItemPosition = getLastVisibleItem()
    val totalItemCount = layoutManager.itemCount

    //calculate if we have enough items and we are not loadingMore items at this time
    if (loadingMore && totalItemCount > previousTotalItemCount) {
      loadingMore = false
      previousTotalItemCount = totalItemCount
    }

    //calculate to ask to load more if needed
    if (
      !loadingMore
      && lastVisibleItemPosition + visibleThreshold > totalItemCount
      && !isShowingLoadMore()
      && !isShowingSkeletonItems()
    ) {
      loadingMore = true
      currentPage++
      onLoadMore(currentPage)
    }
  }

  private fun getLastVisibleItem(): Int {
    return when (layoutManager) {
      is StaggeredGridLayoutManager -> {
        val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
        getLastVisibleItem(lastVisibleItemPositions)
      }
      is GridLayoutManager -> {
        layoutManager.findLastVisibleItemPosition()
      }
      is LinearLayoutManager -> {
        layoutManager.findLastVisibleItemPosition()
      }
      else -> {
        0
      }
    }
  }

  private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
    var maxSize = 0
    for (i in lastVisibleItemPositions.indices) {
      if (i == 0) {
        maxSize = lastVisibleItemPositions[i]
      } else if (lastVisibleItemPositions[i] > maxSize) {
        maxSize = lastVisibleItemPositions[i]
      }
    }
    return maxSize
  }

  fun resetState() {
    this.currentPage = this.startingPageIndex
    this.previousTotalItemCount = 0
    this.loadingMore = true
  }
}
