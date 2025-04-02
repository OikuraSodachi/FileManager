package com.todokanai.filemanager.abstracts.multiselectrecyclerview

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

/** Base class of [SelectionTracker.SelectionPredicate] for [MultiSelectRecyclerAdapter] **/
class BaseSelectionPredicate(private val recyclerView: RecyclerView) : SelectionTracker.SelectionPredicate<Long>() {
    override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
        val holder = recyclerView.findViewHolderForItemId(key)
        return if (holder is RecyclerView.ViewHolder) {
            true
        } else {
            false
        }
    }

    override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
        return true
    }

    override fun canSelectMultiple(): Boolean {
        return true
    }
}