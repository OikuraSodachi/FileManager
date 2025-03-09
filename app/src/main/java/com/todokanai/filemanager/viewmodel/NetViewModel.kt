package com.todokanai.filemanager.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor() :ViewModel(){
    private val module = NetFileModule(
        serverIp = "",
        userId = "",
        userPassword = "",
        defaultDirectory = ""
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