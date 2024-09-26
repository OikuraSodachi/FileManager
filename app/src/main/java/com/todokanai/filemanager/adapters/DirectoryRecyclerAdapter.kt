package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.todokanai.filemanager.R
import com.todokanai.filemanager.base.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.DirectoryHolder
import com.todokanai.filemanager.myobjects.Objects
import kotlinx.coroutines.flow.Flow
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit,
    directoryListNew: Flow<List<File>>,
    lifecycleOwner: LifecycleOwner
): BaseRecyclerAdapter<File,DirectoryHolder>(directoryListNew,lifecycleOwner) {

    private val modeManager = Objects.modeManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view)
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        val directory = itemList[position]
        holder.setHolder(directory)
        holder.itemView.setOnClickListener {
            if(modeManager.isNotMultiSelectMode()) {
                onClick(directory)
            }
        }
    }
}