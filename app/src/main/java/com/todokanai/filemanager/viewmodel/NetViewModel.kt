package com.todokanai.filemanager.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
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

    val itemFlow = module.itemList.map {
        it.map { ftpFile ->
            FileHolderItem(
                absolutePath = ftpFile.name,
                size = ftpFile.size,
                lastModified = ftpFile.timestamp.timeInMillis,
                uri = testUri
            )
        }
    }
    fun onItemClick(context: Context,item: FileHolderItem){}
}