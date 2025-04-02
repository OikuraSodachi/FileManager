package com.todokanai.filemanager.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import java.io.File

class DirectoryHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    private val directoryName = itemView.findViewById<TextView>(R.id.directoryName)
    private val dividerSlot = itemView.findViewById<ImageView>(R.id.divider)

    fun onInit(item: File) {
        directoryName.text = item.name
        dividerSlot.setImageDrawable(getDrawable(itemView.context,R.drawable.baseline_label_important_24))
    }
}