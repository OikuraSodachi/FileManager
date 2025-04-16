package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module: NetFileModule) : NetViewModelLogics() {

    override val dirTree: Flow<List<DirectoryHolderItem>>
        get() = emptyFlow() // Todo

    override val itemList: Flow<List<FileHolderItem>>
        get() = module.currentDirectory.map{ directory ->
            module.ftpListFiles(directory).map {
                FileHolderItem.fromFTPFile(it,directory)
               // it.toFileHolderItem(directory)
            }
        }

    override val isLoggedIn: Flow<Boolean>
        get() = module.loggedIn

    override fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            module.login()
        }
    }

    override fun onItemClick(item: FileHolderItem) {
        viewModelScope.launch {
            val ftpFile = module.fileInfo(item.absolutePath)

            if (ftpFile.isDirectory) {
                module.setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a File")
            }
        }
    }

    override fun toParent() {
        viewModelScope.launch {
            module.toParentDirectory()
        }
    }

    fun test(){
        viewModelScope.launch {
            module.setCurrentDirectory("HDD1")
        }
    }

}