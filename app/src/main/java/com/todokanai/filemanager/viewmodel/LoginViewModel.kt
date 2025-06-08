package com.todokanai.filemanager.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.components.service.NetService
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.tools.independent.loginToFTPServer_td
import com.todokanai.filemanager.viewmodel.logics.LoginViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serverRepo: ServerInfoRepository,
    val ftpClient: FTPClient
): ViewModel(), LoginViewModelLogics {

    override val uiState = serverRepo.serverInfoFlow.map{ serverList ->
        LoginUiState(
            serverList = serverList.map {
                ServerHolderItem(
                    it.name,
                    it.no!!         // Todo: NPE 발생 가능성 확인 필요
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginUiState()
    )

    override fun onServerClick(context: Context,server: ServerHolderItem,onLoginResult: (Boolean)->Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val serverInfo = serverRepo.getById(id = server.id)
            val success = login(serverInfo)
            if(success){
                NetService.currentServer = serverInfo
                context.startForegroundService(Intent(context, NetService::class.java))
                // context.startService(Intent(context, NetService::class.java))
            }else{
                println("login failed")
            }
            withContext(Dispatchers.Main){ onLoginResult(success)}
        }
    }

    override fun deleteServer(server: ServerHolderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.deleteByIndex(server.id)
        }
    }

    override fun saveServerInfo(name: String, ip: String, id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.insert(ServerInfo(name, ip, id, password))
        }
    }

    private fun login(serverInfo: ServerInfo):Boolean{
        return loginToFTPServer_td(
            client = ftpClient,
            serverIp = serverInfo.ip,
            username = serverInfo.id,
            password = serverInfo.password,
            port = 21
        )
    }

    fun test(){
        val temp = ftpClient.isConnected
    }
}

data class LoginUiState(
    val serverList: List<ServerHolderItem> = emptyList()
)