package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.databinding.FilelistRecyclerBinding
import com.todokanai.filemanager.holders.FileItemHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetRecyclerAdapter(
    private val onItemClick: (FileHolderItem) -> Unit
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

    /** Todo: 이거 여기에 두는게 적절한지 의문... **/
    private val _bottomMenuEnabled = MutableStateFlow<Boolean>(false)
    val bottomMenuEnabled = _bottomMenuEnabled.asStateFlow()

    override val selectionId = "netSelectionId"

    override fun onSelectionChanged(index: Int, item: FileHolderItem) {
        if (selectionTracker.selection.contains(index.toLong())) {
            item.isSelected = true
        } else {
            item.isSelected = false
        }
        _bottomMenuEnabled.value = selectionTracker.hasSelection()
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