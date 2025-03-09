package com.todokanai.filemanager.holders

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import java.io.File
import java.text.DateFormat

class FileItemHolder(itemView:View): BaseRecyclerViewHolder<FileHolderItem>(itemView) {

    private val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    private val fileName = itemView.findViewById<TextView>(R.id.fileName)
    private val fileSize = itemView.findViewById<TextView>(R.id.fileSize)
    private val lastModified = itemView.findViewById<TextView>(R.id.lastModified)
    private val multiSelectView = itemView.findViewById<ImageView>(R.id.multiSelectView)

    /** asyncImage 여부 결정 **/
    private fun File.isImage():Boolean{
        var result = false
        val temp = getMimeType_td(this.name)
        if(temp=="video/*" || temp == "image/*" ){
            result = true
        }
        return result
    }

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
        val size =
            if(item.isDirectory()) {
                val subFiles = item.listFiles()
                if(subFiles == null){
                    "null"
                }else {
                    "${subFiles.size} 개"
                }
            } else {
                arrayOf(item).getTotalSize_td().readableFileSize_td()
            }
        thumbnail.setThumbnail(item)
        fileName.text = item.name
        fileName.isSelected = true
        fileSize.text = size
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