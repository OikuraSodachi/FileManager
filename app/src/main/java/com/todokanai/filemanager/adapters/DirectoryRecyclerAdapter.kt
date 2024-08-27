package com.todokanai.filemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.holders.DirectoryHolder
import java.io.File

class DirectoryRecyclerAdapter(
    private val onClick:(File)->Unit,
):RecyclerView.Adapter<DirectoryHolder>() {
    var directoryList = emptyList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryHolder(view)
    }

    override fun getItemCount(): Int {
        return directoryList.size
    }

    override fun onBindViewHolder(holder: DirectoryHolder, position: Int) {
        val directory = directoryList[position]
        holder.setHolder(directory)
        holder.itemView.setOnClickListener { onClick(directory) }
    }
}