package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.ServerHolderItem

interface LoginViewModelLogics {
    fun deleteServer(server: ServerHolderItem)

    fun onServerClick(context: Context, server: ServerHolderItem,onLoginResult: (Boolean)->Unit)

    fun saveServerInfo(name: String, ip: String, id: String, password: String)
}