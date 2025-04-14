package com.todokanai.filemanager.viewmodel.logics

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile

abstract class NetViewModelLogics : ViewModel() {

    init {
        viewModelScope.launch {
            updateUI()
        }
    }

    protected abstract val itemList: Flow<List<FileHolderItem>>

    abstract suspend fun updateUI()

    abstract fun onItemClick(item: FileHolderItem)

    abstract fun setCurrentDirectory(absolutePath: String)

    abstract fun toParent()

    private val testUri: Uri = Uri.EMPTY

    protected fun FTPFile.toFileHolderItem(currentDirectory: String): FileHolderItem {
        return FileHolderItem(
            absolutePath = "${currentDirectory}/${this.name}",
            size = readableFileSize_td(this.size),
            lastModified = this.timestamp.timeInMillis,
        )
    }
}