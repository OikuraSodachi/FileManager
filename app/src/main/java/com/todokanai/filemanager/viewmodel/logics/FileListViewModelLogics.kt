package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.FileListUiState
import kotlinx.coroutines.flow.Flow

interface FileListViewModelLogics {

    val uiState: Flow<FileListUiState>

    fun onDirectoryClick(item: DirectoryHolderItem)

    fun onBackPressed()

    fun onFileClick(context: Context, item: FileHolderItem)

    fun popupMenuList(selected: Array<FileHolderItem>): List<Pair<String, () -> Unit>>

    fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int

}