package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.StorageHolder
import java.io.File

class StorageRecyclerAdapter(
    val onItemClick:(file: File)->Unit
): ListAdapter<File, StorageHolder>(
    object : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }
        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.storage_recycler,parent,false)
        return StorageHolder(view,onItemClick)
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        val item = getItem(position)
        holder.onInit(item)
    }
}