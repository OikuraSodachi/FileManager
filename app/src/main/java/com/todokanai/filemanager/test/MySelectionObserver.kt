package com.todokanai.filemanager.test

import android.annotation.SuppressLint
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver

class MySelectionObserver:SelectionObserver<Long>() {

    override fun onSelectionRestored() {
        println("onSelectionRestored")
        super.onSelectionRestored()
    }

    override fun onSelectionChanged() {
        println("onSelectionChanged")
        super.onSelectionChanged()
    }

    override fun onSelectionRefresh() {
        println("onSelectionRefresh")
        super.onSelectionRefresh()
    }

    @SuppressLint("RestrictedApi")
    override fun onSelectionCleared() {
        println("onSelectionCleared")
        super.onSelectionCleared()
    }

    override fun onItemStateChanged(key: Long, selected: Boolean) {
        println("onItemStateChanged: key: $key, selected: $selected")
        super.onItemStateChanged(key, selected)
    }
}