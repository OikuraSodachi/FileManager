package com.todokanai.filemanager.viewmodel.logics

import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.NetUiState
import kotlinx.coroutines.flow.Flow

interface NetViewModelLogics {
    val uiState: Flow<NetUiState>
    fun onItemClick(item: FileHolderItem)
    fun onItemLongClick(item: FileHolderItem)
    fun onDirectoryClick(item: DirectoryHolderItem)
    fun toParent(onLogout: (Boolean) -> Unit)

}