package com.todokanai.filemanager.viewmodel.logics

import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.data.dataclass.ServerHolderItem

interface NetViewModelLogics {

    //---------
    // LoginFragment part

    fun deleteServer(server: ServerHolderItem)

    fun onServerClick(server: ServerHolderItem)

    fun saveServerInfo(name: String, ip: String, id: String, password: String)

    //
    //-----------------------------------

    fun onItemClick(item: FileHolderItem)
    fun onDirectoryClick(item: DirectoryHolderItem)
    fun toParent()

}