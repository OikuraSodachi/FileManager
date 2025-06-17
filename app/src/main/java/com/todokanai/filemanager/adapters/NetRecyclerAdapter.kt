package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.databinding.FilelistRecyclerBinding
import com.todokanai.filemanager.holders.FileItemHolder

class NetRecyclerAdapter(
    private val onItemClick: (FileHolderItem) -> Unit,
    private val enableBottomMenu:(Boolean)->Unit
) : MultiSelectRecyclerAdapter<FileHolderItem, FileItemHolder>(
    object : DiffUtil.ItemCallback<FileHolderItem>() {
        override fun areItemsTheSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }

        override fun areContentsTheSame(oldItem: FileHolderItem, newItem: FileHolderItem): Boolean {
            return oldItem == newItem
        }
    }
) {


    override val selectionId = "netSelectionId"
    override fun onSelectionInstance(isSelectionNotEmpty: Boolean) {
        enableBottomMenu(isSelectionNotEmpty)
    }

    override fun onSelectionChanged(index: Int, item: FileHolderItem) {
        if (selectionTracker.selection.contains(index.toLong())) {
            item.isSelected = true
        } else {
            item.isSelected = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val binding =
            FilelistRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileItemHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val item = getItem(position)
        holder.run {
            onInit(item)
        }
    }
}