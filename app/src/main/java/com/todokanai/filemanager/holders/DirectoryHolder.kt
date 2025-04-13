package com.todokanai.filemanager.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem

class DirectoryHolder(itemView: View, private val onClick: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val directoryName = itemView.findViewById<TextView>(R.id.directoryName)
    private val dividerSlot = itemView.findViewById<ImageView>(R.id.divider)

    fun onInit(item: DirectoryHolderItem) {
        directoryName.text = item.name
        dividerSlot.setImageDrawable(
            getDrawable(
                itemView.context,
                R.drawable.baseline_label_important_24
            )
        )
        itemView.setOnClickListener {
            onClick(item.absolutePath)
        }
    }
}