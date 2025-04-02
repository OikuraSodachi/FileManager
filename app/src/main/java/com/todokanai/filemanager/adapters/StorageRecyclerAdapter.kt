package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.StorageHolder
import java.io.File

class StorageRecyclerAdapter(
    val onItemClick:(file: File)->Unit
): BaseRecyclerAdapter<File,StorageHolder>() {

    override fun areItemsSame(oldItem: File, newItem: File): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.storage_recycler,parent,false)
        return StorageHolder(view,onItemClick)
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        val item = itemList()[position]
        holder.onInit(item)
    }
}