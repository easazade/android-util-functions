package ir.easazade.androidutils.recyclerview

data class ListState<ITEM>(
  val items: MutableList<ITEM>,
  val page: Int,
  val verticalOffset: Int?
)