package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import kotlinx.coroutines.flow.Flow

abstract class LoginViewModelLogics : ViewModel() {

    abstract val serverListFlow: Flow<List<ServerHolderItem>>

    abstract fun deleteServer(server: ServerHolderItem)

    abstract fun saveServerInfo(ip: String, id: String, password: String)

}