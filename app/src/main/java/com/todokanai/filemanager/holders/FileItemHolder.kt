package com.todokanai.filemanager.holders

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.todokanai.filemanager.R
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.databinding.FilelistRecyclerBinding
import java.io.File
import java.text.DateFormat

class FileItemHolder(
    val binding: FilelistRecyclerBinding,
    private val onClick: (FileHolderItem) -> Unit,
    private val onLongClick: (FileHolderItem)->Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun onInit(item: FileHolderItem) {
        binding.run {
            thumbnail.setThumbnail(item)
            fileName.text = item.name
            fileName.isSelected = true
            fileSize.text = item.size
            lastModified.text = DateFormat.getDateTimeInstance().format(item.lastModified)
        }
        itemView.run{
            setOnClickListener { onClick(item) }
            setOnLongClickListener{
                onLongClick(item)
                true
            }
        }
        onSelectionChanged(item.isSelected)
    }

    private fun ImageView.setThumbnail(file: FileHolderItem) {
        if (file.isImage()) {
            Glide.with(itemView)
                .load(File(file.absolutePath))
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
        binding.run {
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
    }

    fun onDefaultMode() {
        binding.multiSelectView.visibility = View.VISIBLE
    }

    fun onSelectionChanged(isSelected: Boolean) {
        binding.run {
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
}