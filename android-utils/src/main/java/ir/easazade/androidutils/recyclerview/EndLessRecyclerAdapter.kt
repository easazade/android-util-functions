package ir.easazade.androidutils.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ir.easazade.androidutils.R
import ir.easazade.androidutils.recyclerview.ListState.FAILED
import ir.easazade.androidutils.recyclerview.ListState.LOADED
import ir.easazade.androidutils.recyclerview.ListState.LOADING
import timber.log.Timber

class EndLessRecyclerAdapter<ITEM : Any, ITEMID : Any>(
  private val recyclerView: RecyclerView,
  val items: MutableList<ITEM>,
  val listState: RecyclerViewState? = null,
  private val getItemId: (ITEM) -> ITEMID,
  var currentPage: Int,
  private val getNextPageItems: (page: Int) -> Unit,
  private val onClick: (ITEM) -> Unit,
  private val createNewViewHolder: (parent: ViewGroup, viewType: Int) -> ViewHolder,
  private val bindToViewHolder: (viewHolder: ViewHolder, item: ITEM, position: Int) -> Unit,
  private var visibleThreshold: Int,
  @LayoutRes private val loadingLayoutId: Int = R.layout.endless_list_item_loading,
  @LayoutRes private val failedLayoutId: Int = R.layout.endliess_liest_item_failed
) : RecyclerView.Adapter<ViewHolder>() {

  private var askedPage = currentPage

  companion object {
    const val MAIN_ITEM_VIEW_TYPE = 112
    const val LOADER_ITEM_VIEW_TYPE = 102
    const val FAILED_ITEM_VIEW_TYPE = 79
  }

  private var mNetworkState: ListState? = if (items.isNotEmpty()) LOADED else null
  private var totalItemCount = 0
  private var lastVisibleItem = 0
  private var isEndOfList = false
  private var alreadyRequestedForMoreItems = false
  private val mViewHolders = mutableListOf<ViewHolder>()

  init {
    //load more designItems when list reaches bottom
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val llmanager = recyclerView.layoutManager as LinearLayoutManager
        totalItemCount = llmanager.itemCount
        lastVisibleItem = llmanager.findLastVisibleItemPosition()
        if (
          (!hasExtraRow() || (hasExtraRow() && mNetworkState == FAILED))
          && totalItemCount <= (lastVisibleItem + visibleThreshold)
          && !alreadyRequestedForMoreItems
          && !isEndOfList
        ) {
          Timber.d("loading more")
          alreadyRequestedForMoreItems = true
          setState(LOADING)
          askedPage = currentPage + 1
          getNextPageItems(askedPage)
        }
      }
    })
    if (listState?.verticalOffset != null)
      recyclerView.scrollBy(0, listState.verticalOffset)
  }

  override fun getItemViewType(position: Int): Int {
    return if (hasExtraRow() && position == itemCount - 1 && mNetworkState == LOADING)
      LOADER_ITEM_VIEW_TYPE
    else if (hasExtraRow() && position == itemCount - 1 && mNetworkState == FAILED)
      FAILED_ITEM_VIEW_TYPE
    else
      MAIN_ITEM_VIEW_TYPE
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (viewType) {
      MAIN_ITEM_VIEW_TYPE -> {
        val holder = createNewViewHolder(parent, viewType)
        mViewHolders.add(holder)
        holder
      }
      LOADER_ITEM_VIEW_TYPE -> LoadingViewHolder(
        LayoutInflater.from(parent.context).inflate(loadingLayoutId, parent, false)
      )
      FAILED_ITEM_VIEW_TYPE -> FailedViewHolder(
        LayoutInflater.from(parent.context).inflate(failedLayoutId, parent, false)
      )
      else -> throw IllegalArgumentException("view type unknown")
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (holder) {
      is LoadingViewHolder -> {
      }
      is FailedViewHolder -> {
      }
      else -> {
        bindToViewHolder(holder, items[position], position)
        holder.itemView.setOnClickListener {
          onClick(items[holder.adapterPosition])
        }
      }
    }
  }

  override fun getItemCount(): Int = items.size + if (hasExtraRow()) 1 else 0

  private fun hasExtraRow(): Boolean = mNetworkState != null && mNetworkState != LOADED

  fun addMoreItems(newList: List<ITEM>) {
    if (currentPage != askedPage) currentPage = askedPage
    alreadyRequestedForMoreItems = false
    val position = items.size
    items.addAll(newList)
    notifyItemInserted(position)
    setState(LOADED)
  }

  fun showFailure() {
    recyclerView.postDelayed({
      setState(FAILED)
      alreadyRequestedForMoreItems = false
    }, 1000)
  }

  fun setEndOfList() {
    recyclerView.post {
      isEndOfList = true
      setState(LOADED)
    }
  }

  fun updateItem(newItem: ITEM) {
    var index = -1
    items.forEach { listItem ->
      if (getItemId(newItem) == getItemId(listItem)) {
        index = items.indexOf(listItem)
      }
    }
    if (index != -1) {
      items.removeAt(index)
      items.add(index, newItem)
      recyclerView.post {
        notifyItemChanged(index)
      }
    }
  }

  /***
   * removes item and returns the position which item has been removed at
   */
  fun removeItem(itemId: ITEMID): Int {
    var index = -1
    items.forEach { listItem ->
      if (itemId == getItemId(listItem)) {
        index = items.indexOf(listItem)
      }
    }
    if (index != -1) {
      items.removeAt(index)
      recyclerView.post {
        notifyItemRemoved(index)
      }
    }
    return index
  }

  fun addItemAtPosition(item: ITEM, position: Int) {
    items.add(position, item)
    recyclerView.post { notifyItemInserted(position) }
  }

  fun updateItemAndView(itemId: ITEMID, newItem: ITEM?, updateAction: (position: Int, holder: ViewHolder?) -> Unit) {

    fun updateView(position: Int) {
      val llmanager = recyclerView.layoutManager as LinearLayoutManager
      val view = llmanager.findViewByPosition(position)
      if (view != null) {
        Timber.d("view is not null")
        mViewHolders.forEach { holder ->
          if (view == holder.itemView) {
            updateAction(position, holder)
            Timber.d("found holder updating view")
          }
        }
      }
    }

    if (newItem != null) {
      var index = -1
      items.forEach { listItem ->
        if (getItemId(newItem) == getItemId(listItem))
          index = items.indexOf(listItem)
      }
      if (index != -1) {
        items.removeAt(index)
        items.add(index, newItem)
        recyclerView.post {
          updateView(index)
        }
      }
    } else {
      for (i in 0 until items.size) {
        if (getItemId(items[i]) == itemId)
          recyclerView.post {
            updateView(i)
          }
      }
    }
  }

  fun refreshItemWithId(itemId: ITEMID) {
    for (i in 0 until items.size) {
      if (getItemId(items[i]) == itemId)
        notifyItemChanged(i)
    }
  }

  private fun setState(newNetworkState: ListState?) {
    Timber.d(newNetworkState.toString())
    newNetworkState?.let {
      if (items.isNotEmpty()) {
        val previousState = this.mNetworkState
        val hadExtraRow = hasExtraRow()
        this.mNetworkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
          if (hadExtraRow) {
            recyclerView.post { notifyItemRemoved(items.size) }
          } else {
            recyclerView.post { notifyItemInserted(items.size) }
          }
        } else if (hasExtraRow && previousState !== newNetworkState) {
          recyclerView.post { notifyItemChanged(itemCount - 1) }
        }
      }
    }
  }

  class LoadingViewHolder(root: View) : ViewHolder(root)

  class FailedViewHolder(root: View) : ViewHolder(root)
}