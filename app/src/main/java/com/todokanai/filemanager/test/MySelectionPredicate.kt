package com.todokanai.filemanager.test

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.holders.FileItemHolder

class MySelectionPredicate(private val recyclerView: RecyclerView) :
    SelectionTracker.SelectionPredicate<Long>() {
    override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
        val holder = recyclerView.findViewHolderForItemId(key)
        return if (holder is FileItemHolder) {
            //holder.element.text == "YES"
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