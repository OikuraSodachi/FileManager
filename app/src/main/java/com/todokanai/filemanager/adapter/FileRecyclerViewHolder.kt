package com.todokanai.filemanager.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R

class FileRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val typeImage = itemView.findViewById<ImageView>(R.id.typeImage)
    val fileName = itemView.findViewById<TextView>(R.id.fileNameText)
    val fileDate = itemView.findViewById<TextView>(R.id.dateText)
    val fileSize = itemView.findViewById<TextView>(R.id.fileSizeText)
    val multiSelector = itemView.findViewById<RadioButton>(R.id.multiSelector)
}