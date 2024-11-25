package com.todokanai.filemanager.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import java.io.File

class DirectoryHolder(itemView:View): BaseRecyclerViewHolder<File>(itemView) {
    private val directoryName = itemView.findViewById<TextView>(R.id.directoryName)
    private val dividerSlot = itemView.findViewById<ImageView>(R.id.divider)

    override fun onInit(item: File) {
        directoryName.text = item.name
        dividerSlot.setImageDrawable(getDrawable(itemView.context,R.drawable.baseline_label_important_24))
    }
}