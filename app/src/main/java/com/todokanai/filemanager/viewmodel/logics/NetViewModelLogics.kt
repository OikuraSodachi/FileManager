package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo

interface NetViewModelLogics {

    //---------
    // LoginFragment part

    fun deleteServer(server: ServerHolderItem)

    fun onServerClick(context: Context, server: ServerHolderItem)

    fun saveServerInfo(name: String, ip: String, id: String, password: String)

    //
    //-----------------------------------

    fun onItemClick(item: FileHolderItem)
    fun onDirectoryClick(item: DirectoryHolderItem)
    fun toParent()
    //-----------
    fun login(serverInfo: ServerInfo):Boolean
    fun getParent():String?
    suspend fun setCurrentDirectory(directory: String)

}