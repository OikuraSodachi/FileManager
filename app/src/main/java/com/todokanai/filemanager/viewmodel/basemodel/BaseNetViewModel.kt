package com.todokanai.filemanager.viewmodel.basemodel

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import kotlinx.coroutines.flow.combine
import org.apache.commons.net.ftp.FTPFile

abstract class BaseNetViewModel(private val module:NetFileModule) : ViewModel() {

    abstract fun onItemClick(item:FileHolderItem)

    fun toParent() = module.toParentDirectory()

    abstract fun toFileHolderItem(ftpFile:FTPFile, currentDirectory:String):FileHolderItem

    val itemFlow
        get() = combine(
            module.itemList,
            module.currentDirectory
        ) {
          items,directory ->
            items.map {
                toFileHolderItem(it, directory)
            }
        }
}