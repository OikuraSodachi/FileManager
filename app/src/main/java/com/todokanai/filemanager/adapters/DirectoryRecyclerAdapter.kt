package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.DirectoryHolder
import kotlinx.coroutines.flow.Flow
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit,
    directoryListNew: Flow<List<File>>,
    lifecycleOwner: LifecycleOwner,
    val isNotMultiSelectMode:()->Boolean
): BaseRecyclerAdapter<File,DirectoryHolder>(directoryListNew,lifecycleOwner) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view)
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        val directory = itemList[position]
        holder.setHolder(directory)
        holder.itemView.setOnClickListener {
            if(isNotMultiSelectMode()) {
                onClick(directory)
            }
        }
    }
}