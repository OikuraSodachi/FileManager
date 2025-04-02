package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerAdapter
import com.todokanai.filemanager.holders.DirectoryHolder
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit
): BaseRecyclerAdapter<File,DirectoryHolder>() {
    override fun areItemsSame(oldItem: File, newItem: File): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        val directory = itemList()[position]
        holder.onInit(directory)
    }
}