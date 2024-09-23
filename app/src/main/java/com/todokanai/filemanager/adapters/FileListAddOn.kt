package com.todokanai.filemanager.adapters

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView

class FileListAddOn(val recyclerView: RecyclerView) {

    fun attachTo(
        adapter:FileListRecyclerAdapter
    ){
        adapter.selectionTracker = SelectionTracker.Builder(
            "my-selection-id",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            FileDetailLookup(recyclerView),
            StorageStrategy.createLongStorage()
        )
            .build()
    }

}