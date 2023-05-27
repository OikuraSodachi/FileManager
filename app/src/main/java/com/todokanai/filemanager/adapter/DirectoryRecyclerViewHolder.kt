package com.todokanai.filemanager.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R

class DirectoryRecyclerViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val directoryText = itemView.findViewById<TextView>(R.id.directoryName)
}