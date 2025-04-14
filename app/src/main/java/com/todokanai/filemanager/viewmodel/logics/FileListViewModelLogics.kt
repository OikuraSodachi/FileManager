package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import kotlinx.coroutines.flow.Flow
import org.apache.commons.net.ftp.FTPFile
import java.io.File

abstract class FileListViewModelLogics : ViewModel() {

    abstract val dirTree: Flow<List<DirectoryHolderItem>>

    abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract val notAccessible: Flow<Boolean>

    abstract fun onDirectoryClick(absolutePath: String)

    abstract fun onFileClick(context: Context, item: File)

    protected fun FTPFile.toFileHolderItem(): FileHolderItem {
        return FileHolderItem(
            absolutePath = this.name,
            size = readableFileSize_td(this.size),
            lastModified = this.timestamp.time.time
            //      uri = null
        )
    }
}