package com.todokanai.filemanager.abstracts.multiselectrecyclerview

import androidx.recyclerview.selection.SelectionTracker.SelectionObserver

/** Todo: warning(?) : callback is called rapidly **/
class BaseSelectionObserver(val callback:()->Unit):SelectionObserver<Long>() {

    override fun onSelectionChanged() {
        super.onSelectionChanged()
        callback()
    }
}