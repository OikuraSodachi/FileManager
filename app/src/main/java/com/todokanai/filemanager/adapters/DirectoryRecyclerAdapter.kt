package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.DirectoryHolder
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick: (File) -> Unit
) : ListAdapter<File, DirectoryHolder>(DirectoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler, parent, false)
        return DirectoryHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        holder.onInit(getItem(position))
    }
}

private class DirectoryDiffCallback : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }

}