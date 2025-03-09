package com.todokanai.filemanager.holders

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.Glide
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import java.text.DateFormat

class FileItemHolder(itemView:View): BaseRecyclerViewHolder<FileHolderItem>(itemView) {

    private val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    private val fileName = itemView.findViewById<TextView>(R.id.fileName)
    private val fileSize = itemView.findViewById<TextView>(R.id.fileSize)
    private val lastModified = itemView.findViewById<TextView>(R.id.lastModified)
    private val multiSelectView = itemView.findViewById<ImageView>(R.id.multiSelectView)

    /** file의 extension에 따른 기본 thumbnail 값 */
    private fun FileHolderItem.thumbnail(context: Context) : Drawable? {
        return if (this.isDirectory()) {
            getDrawable(context, R.drawable.ic_baseline_folder_24)
        } else {
            when (this.extension()) {
                "pdf" -> {
                    getDrawable(context, R.drawable.ic_pdf)
                }
                else -> {
                    getDrawable(
                        context,
                        R.drawable.ic_baseline_insert_drive_file_24
                    )
                }
            }
        }
    }

    override fun onInit(item: FileHolderItem) {
        thumbnail.setThumbnail(item)
        fileName.text = item.name()
        fileName.isSelected = true
        fileSize.text = item.sizeText()
        lastModified.text = DateFormat.getDateTimeInstance().format(item.lastModified)
    }

    private fun ImageView.setThumbnail(file: FileHolderItem){
        if(file.isImage()){
            Glide.with(itemView)
                .load(file.uri)
                .into(this)
        } else{
            this.setImageDrawable(file.thumbnail(itemView.context))
        }
    }

    fun multiSelectMode(isSelected: Boolean){
        multiSelectView.visibility = View.VISIBLE
        if(isSelected){
            multiSelectView.setImageDrawable(getDrawable(itemView.context,R.drawable.baseline_check_24))
        } else{
            multiSelectView.setImageDrawable(null)
        }
    }
    fun onDefaultMode(){
        multiSelectView.visibility = View.VISIBLE
    }


    override fun onSelectionChanged(isSelected: Boolean) {
        super.onSelectionChanged(isSelected)
        if(isSelected){
            multiSelectView.visibility = View.VISIBLE
            multiSelectView.setImageDrawable(getDrawable(itemView.context,R.drawable.baseline_check_24))
            itemView.setBackgroundColor(Color.GRAY)
        } else{
            multiSelectView.visibility = View.GONE
            itemView.setBackgroundColor(0)
            multiSelectView.setImageDrawable(null)
        }
    }
}