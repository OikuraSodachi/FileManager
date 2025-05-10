package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem

interface FileListViewModelLogics {

    fun onDirectoryClick(item: DirectoryHolderItem)

    fun onFileClick(context: Context, item: FileHolderItem)

    fun setCurrentDirectory(directory: String)

}