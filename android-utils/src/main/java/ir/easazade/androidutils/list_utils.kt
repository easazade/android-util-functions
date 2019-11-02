@file:Suppress("FunctionName")

package ir.easazade.androidutils

import java.util.Stack

/**
 * @returns the page items of the given page based on the page number and count of items in each page
 */
fun <T> MutableList<T>._getPagination(page: Int, count: Int): MutableList<T> {
  val from = count * (if (page < 1) 0 else (page - 1))
  val until = count * (if (page < 1) 1 else page)
  if (from in 0 until size) {
    return if (until in 0 until size) {
      subList(from, until)
    } else {
      subList(from, size)
    }
  }
  return mutableListOf()
}

/****
 * @returns a pair of lists that each contains the different items each provided list argument have
 * if there is no difference both returned list in Pair object will be empty
 */
fun <T> _compareLists(
  first: List<T>,
  second: List<T>,
  predicate: (itemOfFirst: T, itemOfSecond: T) -> Boolean
): Pair<MutableList<T>, MutableList<T>> {
  val firstListDifferences = mutableListOf<T>()
  val secondListDifferences = mutableListOf<T>()
  for (i in 0 until first.size) {
    val firstVal = first[i]
    var secondListHasValueOfFirstList = false
    for (j in 0 until second.size) {
      val secondVal = second[j]
      if (predicate(firstVal, secondVal))
        secondListHasValueOfFirstList = true
    }
    if (!secondListHasValueOfFirstList)
      firstListDifferences.add(firstVal)
  }
  for (k in 0 until second.size) {
    val secondVal = second[k]
    var firstListHasValueOfSecondList = false
    for (l in 0 until first.size) {
      val firstVal = first[l]
      if (predicate(secondVal, firstVal))
        firstListHasValueOfSecondList = true
    }
    if (!firstListHasValueOfSecondList)
      secondListDifferences.add(secondVal)
  }
  return Pair(firstListDifferences, secondListDifferences)
}

///**
// * removes items that matches the predicate and returns the
// */
//private fun <T> MutableList<T>._removeIf(predicate: (T) -> Boolean): List<Int> {
//  val indexes = Stack<Int>()
//  forEach {
//    if (predicate(it))
//      indexes.add(indexOf(it))
//  }
//  val copiedIndexes = mutableListOf<Int>()
//  copiedIndexes.addAll(indexes)
//  while (indexes.isNotEmpty()) {
//    val index = indexes.pop()
//    removeAt(index)
//  }
//  return copiedIndexes
//}
//
//fun <T> MutableList<T>.hasMatchingItems(
//  matchingItems: List<T>,
//  predicate: (listItem: T, matchingItem: T) -> Boolean
//): List<Int> = _removeIf { listItem ->
//  var flag = false
//  matchingItems.forEach { matchingItem ->
//    if (predicate(listItem, matchingItem))
//      flag = true
//  }
//  flag
//}

/***
 * add items to the list from newItems if they do not match the predicate with any items in list
 * @return list of indexes from newItems list that were added to the list
 */
fun <T> MutableList<T>._addAllIfNotExists(
  newItems: List<T>,
  matchingPredicate: (t1: T, t2: T) -> Boolean
): List<Int> {
  val indexOfNewItemsCanBeAdded = Stack<Int>()
  for (i in 0 until newItems.size) {
    var canBeAdded = true
    for (j in 0 until size) {
      if (matchingPredicate(newItems[i], this[j]))
        canBeAdded = false
    }
    if (canBeAdded) indexOfNewItemsCanBeAdded.add(i)
  }
  val addedItemsIndexes = mutableListOf<Int>().apply { addAll(indexOfNewItemsCanBeAdded) }
  while (indexOfNewItemsCanBeAdded.isNotEmpty()) {
    val index = indexOfNewItemsCanBeAdded.pop()
    add(newItems[index])
  }
  return addedItemsIndexes
}

/***
 * add items to the list from newItems if they do not with any items in list (don't already exists)
 * and if they exists they will be replaced by the newer version from newItems list
 * @return list of indexes from newItems list that were added to the list or replaced another item in list
 */
fun <T> MutableList<T>._addOrUpdateAll(
  newItems: List<T>,
  matchingPredicate: (t1: T, t2: T) -> Boolean
): List<Int> {
  val indexOfNewItemsCanBeAdded = Stack<Int>()
  val indexOfItemsShouldBeUpdated = Stack<Pair<Int, Int>>()
  for (i in 0 until newItems.size) {
    var newItem = true
    for (j in 0 until size) {
      if (matchingPredicate(newItems[i], this[j])) {
        newItem = false
        indexOfItemsShouldBeUpdated.add(Pair(i, j))
      }
    }
    if (newItem)
      indexOfNewItemsCanBeAdded.add(i)
  }
  val addedItemsIndexes = mutableListOf<Int>().apply {
    addAll(indexOfItemsShouldBeUpdated.map { it.first })
    addAll(indexOfNewItemsCanBeAdded)
  }
  while (indexOfItemsShouldBeUpdated.isNotEmpty()) {
    val pair = indexOfItemsShouldBeUpdated.pop()
    val (newItemIndex, oldItemIndex) = pair
    removeAt(oldItemIndex)
    add(oldItemIndex, newItems[newItemIndex])
  }
  while (indexOfNewItemsCanBeAdded.isNotEmpty()) {
    val index = indexOfNewItemsCanBeAdded.pop()
    add(newItems[index])
  }
  return addedItemsIndexes
}

/**
 * checks if any item in the list matches our parameter item using predicate to find a match
 */
fun <T> MutableList<T>._contains(
  item: T,
  matchingPredicate: (t1: T, t2: T) -> Boolean
): Boolean {
  var lisHasItem = false
  forEach { listItem ->
    if (matchingPredicate(listItem, item))
      lisHasItem = true
  }
  return lisHasItem
}

fun <T> List<T>._hasMatch(
  matchingPredicate: (listItem: T) -> Boolean
): T? {
  var match: T? = null
  forEach { listItem ->
    if (matchingPredicate(listItem))
      match = listItem
  }
  return match
}
