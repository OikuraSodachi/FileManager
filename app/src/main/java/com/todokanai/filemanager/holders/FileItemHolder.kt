package com.todokanai.filemanager.holders

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.todokanai.filemanager.R
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import java.text.DateFormat

class FileItemHolder(itemView: View, private val onClick: (FileHolderItem) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    private val fileName = itemView.findViewById<TextView>(R.id.fileName)
    private val fileSize = itemView.findViewById<TextView>(R.id.fileSize)
    private val lastModified = itemView.findViewById<TextView>(R.id.lastModified)
    private val multiSelectView = itemView.findViewById<ImageView>(R.id.multiSelectView)

    fun onInit(item: FileHolderItem) {
        thumbnail.setThumbnail(item)
        fileName.text = item.name
        fileName.isSelected = true
        fileSize.text = item.size
        lastModified.text = DateFormat.getDateTimeInstance().format(item.lastModified)
        itemView.setOnClickListener { onClick(item) }
        onSelectionChanged(item.isSelected)
    }

    private fun ImageView.setThumbnail(file: FileHolderItem) {
        if (file.isImage()) {
            Glide.with(itemView)
                .load(file.file())
                .into(this)
        } else {
            /** Todo: icon 가져오는 작업을 ViewModel 쪽으로 옮겨야 할 듯? **/
            val icon: Drawable? =
                if (file.isDirectory) {
                    getDrawable(context, R.drawable.ic_baseline_folder_24)
                } else if (file.extension() == "pdf") {
                    getDrawable(context, R.drawable.ic_pdf)
                } else {
                    getDrawable(context, R.drawable.ic_baseline_insert_drive_file_24)
                }
            this.setImageDrawable(icon)
        }
    }

    fun multiSelectMode(isSelected: Boolean) {
        multiSelectView.visibility = View.VISIBLE
        if (isSelected) {
            multiSelectView.setImageDrawable(
                getDrawable(
                    itemView.context,
                    R.drawable.baseline_check_24
                )
            )
        } else {
            multiSelectView.setImageDrawable(null)
        }
    }

    fun onDefaultMode() {
        multiSelectView.visibility = View.VISIBLE
    }

    fun onSelectionChanged(isSelected: Boolean) {
        if (isSelected) {
            multiSelectView.visibility = View.VISIBLE
            multiSelectView.setImageDrawable(
                getDrawable(
                    itemView.context,
                    R.drawable.baseline_check_24
                )
            )
            itemView.setBackgroundColor(Color.GRAY)
        } else {
            multiSelectView.visibility = View.GONE
            itemView.setBackgroundColor(0)
            multiSelectView.setImageDrawable(null)
        }
    }
}