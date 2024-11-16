package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.StorageHolder
import kotlinx.coroutines.flow.Flow
import java.io.File

class StorageRecyclerAdapter(
    val onItemClick:(file: File)->Unit,
    itemFlow: Flow<List<File>>,
    lifecycleOwner: LifecycleOwner
): BaseRecyclerAdapter<File,StorageHolder>(itemFlow =itemFlow ,lifecycleOwner = lifecycleOwner) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.storage_recycler,parent,false)
        return StorageHolder(view)
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        val item = itemList[position]
        holder.run{
            setDataNew(item)
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}