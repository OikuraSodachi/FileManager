package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import kotlinx.coroutines.flow.Flow
import java.io.File

abstract class FileListViewModelLogics : ViewModel() {

    abstract val dirTree: Flow<List<File>>

    abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract fun onDirectoryClick(file: File)

    abstract fun onFileClick(context: Context, item: File)

    protected fun File.toFileHolderItem(): FileHolderItem {
        val sizeText: String =
            if (this.isDirectory) {
                "${this.listFiles()?.size} 개"
            } else {
                readableFileSize_td(this.length())
            }

        return FileHolderItem(
            absolutePath = this.absolutePath,
            size = sizeText,
            lastModified = this.lastModified(),
            uri = this.toUri()
        )
    }

    /** Todokanai
     *
     *  == File.dirTree_td() **/
    protected fun File.dirTree(): List<File> {
        val result = mutableListOf<File>()
        var now = this
        while (now.parentFile != null) {
            result.add(now)
            now = now.parentFile!!
        }
        return result.reversed()
    }
}