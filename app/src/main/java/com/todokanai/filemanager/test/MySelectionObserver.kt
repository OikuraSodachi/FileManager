package com.todokanai.filemanager.test

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import java.io.File

class MySelectionObserver(
    val selectionTracker: SelectionTracker<Long>,
    val callback: (List<File>) -> Unit,
    val itemList: () -> List<File>
) : SelectionObserver<Long>() {

    override fun onSelectionChanged() {
        callback(
            selectionTracker.selection.map {
                itemList()[it.toInt()]
            }
        )
        super.onSelectionChanged()
    }
}