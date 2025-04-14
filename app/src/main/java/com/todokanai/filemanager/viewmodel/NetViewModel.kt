package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module: NetFileModule) : NetViewModelLogics() {

    override val dirTree: Flow<List<DirectoryHolderItem>>
        get() = emptyFlow() // Todo

    override val itemList: Flow<List<FileHolderItem>>
        get() = combine(
            module.itemList,
            module.currentDirectory
        ) { items, directory ->
            items.map {
                it.toFileHolderItem(directory)
            }
        }

    override fun onItemClick(item: FileHolderItem) {
        viewModelScope.launch {
            if (item.isDirectory) {
                module.setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a File")
            }
        }
    }

    override fun toParent() {
        viewModelScope.launch {
            getParentAbsolutePath_td(module.currentDirectory.value)?.let {
                module.setCurrentDirectory(
                    it
                )
            }
        }
    }
}