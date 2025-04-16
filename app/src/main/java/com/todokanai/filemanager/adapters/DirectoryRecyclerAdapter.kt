package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todokanai.filemanager.R
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.holders.DirectoryHolder

class DirectoryRecyclerAdapter(
    private val onClick: (DirectoryHolderItem) -> Unit
) : ListAdapter<DirectoryHolderItem, DirectoryHolder>(
    object : DiffUtil.ItemCallback<DirectoryHolderItem>() {
        override fun areItemsTheSame(
            oldItem: DirectoryHolderItem,
            newItem: DirectoryHolderItem
        ): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }

        override fun areContentsTheSame(
            oldItem: DirectoryHolderItem,
            newItem: DirectoryHolderItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler, parent, false)
        return DirectoryHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        holder.onInit(getItem(position))
    }
}