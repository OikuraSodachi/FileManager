package com.todokanai.filemanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import java.io.File

class DirectoryRecyclerAdapter(private val onItemClick:(directory:File)->Unit) : RecyclerView.Adapter<DirectoryRecyclerViewHolder>(){

    var directoryTree = emptyList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_recycler,parent,false)
        return DirectoryRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return directoryTree.size
    }

    override fun onBindViewHolder(holder: DirectoryRecyclerViewHolder, position: Int) {
        val directory = directoryTree[position]
        holder.directoryText.text = directory.name
        holder.itemView.setOnClickListener {
            onItemClick(directory)
            notifyItemChanged(position)
        }
    }


}