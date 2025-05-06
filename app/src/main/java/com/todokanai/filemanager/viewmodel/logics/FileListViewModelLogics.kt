package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow

interface FileListViewModelLogics {

    val dirTree: Flow<List<DirectoryHolderItem>>

    val fileHolderList: Flow<List<FileHolderItem>>

    fun onDirectoryClick(item: DirectoryHolderItem)

    fun onFileClick(context: Context, item: FileHolderItem)

    fun setCurrentDirectory(directory: String)

}