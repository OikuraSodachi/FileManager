package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.holders.DirectoryHolder
import kotlinx.coroutines.flow.Flow
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit,
    directoryListNew: Flow<List<File>>,
    val isNotMultiSelectMode:()->Boolean
): BaseRecyclerAdapter<File>(directoryListNew) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<File>, position: Int) {
        super.onBindViewHolder(holder, position)
        val directory = itemList[position]
        holder.itemView.setOnClickListener {
            if(isNotMultiSelectMode()) {
                onClick(directory)
            }
        }
    }
}