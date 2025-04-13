package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow
import java.io.File

abstract class FileListViewModelLogics : ViewModel() {

    abstract val dirTree: Flow<List<DirectoryHolderItem>>

    abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract fun onDirectoryClick(absolutePath: String)

    abstract fun onFileClick(context: Context, item: File)
}