package com.todokanai.filemanager.viewmodel.logics

import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem

interface NetViewModelLogics {

    fun onItemClick(item: FileHolderItem)
    fun onDirectoryClick(item: DirectoryHolderItem)
    fun toParent(onLogout: () -> Unit)

}