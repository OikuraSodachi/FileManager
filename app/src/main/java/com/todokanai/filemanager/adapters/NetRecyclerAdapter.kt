package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.holders.FileItemHolder

class NetRecyclerAdapter(
    private val onItemClick:(FileHolderItem)->Unit
): BaseRecyclerAdapter<FileHolderItem,FileItemHolder>() {

    override fun areItemsSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler, parent, false)
        return FileItemHolder(view,onItemClick)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val item = itemList()[position]
        holder.run{
            onInit(item)
        }
    }
}