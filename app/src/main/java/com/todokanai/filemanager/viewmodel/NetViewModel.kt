package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor() :ViewModel(){
    private val module = NetFileModule(
        serverIp = "",
        userId = "",
        userPassword = "",
        defaultDirectory = ""
    )

    val itemFlow = module.itemList
    fun onItemClick(context: Context,item: FileHolderItem){}
}