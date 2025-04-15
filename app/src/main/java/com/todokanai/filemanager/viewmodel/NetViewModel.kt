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
        get() = module.itemList.map{ items ->
            items.map { it.toFileHolderItem() }
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
            if (item.isDirectory) {
                module.setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a File")
            }
        }
    }

    override fun toParent() {

    }

    fun test(){
        viewModelScope.launch {
            val temp = module.ftpListFiles().map{it.name}
            println(temp)
        }
    }
}