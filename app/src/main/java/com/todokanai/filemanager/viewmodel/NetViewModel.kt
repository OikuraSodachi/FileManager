package com.todokanai.filemanager.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor() :ViewModel(){
    private val module = NetFileModule(
        serverIp = Variables.netIp,
        userId = Variables.netId,
        userPassword = Variables.netPassword,
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