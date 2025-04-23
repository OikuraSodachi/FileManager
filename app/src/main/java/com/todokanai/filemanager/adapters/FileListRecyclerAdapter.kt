package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.todokanai.filemanager.abstracts.multiselectrecyclerview.MultiSelectRecyclerAdapter
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.databinding.FilelistRecyclerBinding
import com.todokanai.filemanager.holders.FileItemHolder

class FileListRecyclerAdapter(
    private val onFileClick: (FileHolderItem) -> Unit
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

    /** Todo: 이거 LiveData 형태로 여기에 두는게 적절한지 의문... **/
    private val _bottomMenuEnabled = MutableLiveData<Boolean>(false)
    val bottomMenuEnabled: LiveData<Boolean>
        get() = _bottomMenuEnabled

    override val selectionId = "selectionId"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val binding = FilelistRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FileItemHolder(binding, onFileClick)
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        val item = getItem(position)
        holder.run {
            onInit(item)
        }
    }

    override fun onSelectionChanged(index: Int, item: FileHolderItem) {
        if (selectionTracker.selection.contains(index.toLong())) {
            item.isSelected = true
        } else {
            item.isSelected = false
        }

        _bottomMenuEnabled.value = selectionTracker.hasSelection()
    }
}
