package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.databinding.FilelistRecyclerBinding
import com.todokanai.filemanager.holders.FileItemHolder

class FileListRecyclerAdapter(
    private val onFileClick: (FileHolderItem) -> Unit,
    private val onFileLongClick:(FileHolderItem)->Unit
) : ListAdapter<FileHolderItem, FileItemHolder>(
    object : DiffUtil.ItemCallback<FileHolderItem>() {
        override fun areItemsTheSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }

        override fun areContentsTheSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val binding =
            FilelistRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileItemHolder(binding, onFileClick, onFileLongClick)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val item = getItem(position)
        holder.run {
            onInit(item)
        }
    }
}