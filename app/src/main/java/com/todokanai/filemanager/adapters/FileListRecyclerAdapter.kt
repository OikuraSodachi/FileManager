package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.holders.FileItemHolder

class FileListRecyclerAdapter(
    private val onFileClick:(FileHolderItem)->Unit
): MultiSelectRecyclerAdapter<FileHolderItem,FileItemHolder>() {

    override fun areItemsSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    private val _bottomMenuEnabled = MutableLiveData<Boolean>(false)
    val bottomMenuEnabled : LiveData<Boolean>
        get() = _bottomMenuEnabled

    fun fetchSelectedItems() = selectedItems().toTypedArray()
    override val selectionId = "selectionId"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler,parent,false)
        return FileItemHolder(view,onFileClick)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val item = itemList()[position]

        holder.run {
            onInit(item)
        }
    }

    override fun observerCallback() {
        /** position of item (starts from 0 ) **/
        val position = selectionTracker.selection.map{it.toInt()}
        println("observerCallback: $position")
    }

}