package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.FileListUiState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileListViewModelLogics {

    val uiState: Flow<FileListUiState>

    fun refresh(currentDirectory: String)

    fun onDirectoryClick(item: DirectoryHolderItem)

    fun onBackPressed(currentDirectory: String)

    fun onFileClick(context: Context, item: FileHolderItem)

    fun onFileLongClick(item: FileHolderItem)

    fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int

    fun renameFile(file: File, newName:String)
}