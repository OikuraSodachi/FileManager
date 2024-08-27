package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.StorageHolder
import java.io.File

class StorageRecyclerAdapter(val onItemClick:(file: File)->Unit):RecyclerView.Adapter<StorageHolder>() {
    var storageList = emptyList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.storage_recycler,parent,false)
        return StorageHolder(view)
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    override fun onBindViewHolder(holder: StorageHolder, position: Int) {
        val item = storageList[position]
        holder.run{
            setDataNew(item)
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}