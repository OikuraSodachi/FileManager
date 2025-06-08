package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.viewmodel.LoginUiState
import kotlinx.coroutines.flow.Flow

interface LoginViewModelLogics {

    val uiState : Flow<LoginUiState>

    fun deleteServer(server: ServerHolderItem)

    fun onServerClick(context: Context, server: ServerHolderItem,onLoginResult: (Boolean)->Unit)

    fun saveServerInfo(name: String, ip: String, id: String, password: String)
}
