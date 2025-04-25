package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow

abstract class FileListViewModelLogics : ViewModel() {

    protected abstract val dirTree: Flow<List<DirectoryHolderItem>>

    protected abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract fun onDirectoryClick(item: DirectoryHolderItem)

    abstract fun onFileClick(context: Context, item: FileHolderItem)

    protected abstract fun setCurrentDirectory(directory: String)

}