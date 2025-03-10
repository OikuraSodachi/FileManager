package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.holders.FileItemHolder
import kotlinx.coroutines.flow.Flow

class NetRecyclerAdapter(
    private val onItemClick:(FileHolderItem)->Unit,
    itemFlow: Flow<List<FileHolderItem>>
): BaseRecyclerAdapter<FileHolderItem>(itemFlow) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filelist_recycler, parent, false)
        return FileItemHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FileHolderItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = itemList[position]
        holder.run{
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}