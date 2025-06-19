package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.FileListUiState
import kotlinx.coroutines.flow.Flow

interface FileListViewModelLogics {

    val uiState: Flow<FileListUiState>

    fun onDirectoryClick(item: DirectoryHolderItem,mode:Int)

    fun onBackPressed(mode:Int)

    fun onFileClick(context: Context, item: FileHolderItem,mode:Int)

    fun onFileLongClick(item: FileHolderItem,mode:Int)

    fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int

}