package com.todokanai.filemanager.holders

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.R
import com.todokanai.filemanager.abstracts.BaseRecyclerViewHolder
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import java.io.File

class StorageHolder(itemView: View): BaseRecyclerViewHolder<File>(itemView) {

    private val storage = itemView.findViewById<TextView>(R.id.storageHolder_storageText)
    private val size = itemView.findViewById<TextView>(R.id.storageHolder_sizeText)
    private val progress = itemView.findViewById<ProgressBar>(R.id.storageHolder_progressBar)

    override fun onInit(item: File) {
        this.storage.text = item.absolutePath
        this.size.text = storageSize(item)
        this.progress.progress = progress(item)
    }

    fun setDataNew(file: File){
        this.storage.text = file.absolutePath
        this.size.text = storageSize(file)
        this.progress.progress = progress(file)
    }

    private fun progress(file:File):Int{
        val used = file.totalSpace - file.freeSpace
        return (used*100/file.totalSpace).toInt()
    }

    private fun storageSize(file:File):String{
        val used = file.totalSpace - file.freeSpace
        val usedSpace = used.readableFileSize_td()
        val total = file.totalSpace.readableFileSize_td()
        return "$usedSpace/$total"
    }
}