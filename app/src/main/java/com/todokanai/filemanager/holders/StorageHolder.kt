package com.todokanai.filemanager.holders

import androidx.recyclerview.widget.RecyclerView
import com.todokanai.filemanager.databinding.StorageRecyclerBinding
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import java.io.File

class StorageHolder(val binding:StorageRecyclerBinding, private val onClick: (File) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun onInit(item: File) {
        binding.run {
            storageHolderStorageText.text = item.absolutePath
            storageHolderSizeText.text = storageSize(item)
            storageHolderProgressBar.progress = progress(item)
        }
        itemView.setOnClickListener { onClick(item) }
    }

    private fun progress(file: File): Int {
        val used = file.totalSpace - file.freeSpace
        return (used * 100 / file.totalSpace).toInt()
    }

    private fun storageSize(file: File): String {
        val used = file.totalSpace - file.freeSpace
        val usedSpace = readableFileSize_td(used)
        val total = readableFileSize_td(file.totalSpace)
        return "$usedSpace/$total"
    }
}