package com.todokanai.filemanager.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(private val dsRepo:DataStoreRepository) :ViewModel(){

    val ip = dsRepo.serverIpFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val id = dsRepo.userIdFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val password = dsRepo.userPasswordFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    private val module = NetFileModule(
        serverIp = {ip.value},
        userId = {id.value},
        userPassword = {password.value},
        defaultDirectory = Variables.defaultDirectory
    )

    private val testUri: Uri = Uri.EMPTY

    val itemFlow = combine(
        module.itemList,
        module.currentDirectory
    ) {
        items,directory ->
        items.map {
            FileHolderItem(
                absolutePath = "${directory}/${it.name}",
                size = it.size,
                lastModified = it.timestamp.timeInMillis,
                uri = testUri
            )
        }
    }

    fun onItemClick(context: Context,item: FileHolderItem){
        if(item.isDirectory()) {
            module.setCurrentDirectory(item.absolutePath)
        }else{
            println("this is a File")
        }
    }
}