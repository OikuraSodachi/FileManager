package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.repository.LoginUiRepository
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.tools.NetFileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val serverRepo:ServerInfoRepository,
    val uiRepo: LoginUiRepository,
    val module:NetFileModule
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            uiRepo.serverListFlow.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        serverList = it.map {
                            ServerHolderItem(
                                name = it.name,
                                id = it.no!!  // Todo: NPE 발생 가능성 확인 필요
                            )
                        }
                    )
                }
            }
        }
        viewModelScope.launch {
            uiRepo.loggedIn.collect {
                _uiState.update { currentState ->
                    currentState.copy(loggedIn = it)
                }
            }
        }

    }

    fun onServerClick(server: ServerHolderItem) {
        viewModelScope.launch(Dispatchers.Default) {
            val temp = serverRepo.getById(server.id)

            module.login(
                serverIp = temp.ip,
                username = temp.id,
                password = temp.password,
                port = 21
            )
            uiRepo.run {
                setCurrentServer(serverRepo.getById(server.id))
                setLoggedIn(true)
            }
        }
    }

    fun deleteServer(server: ServerHolderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.deleteByIndex(server.id)
        }
    }

    fun saveServerInfo(name: String, ip: String, id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.insert(ServerInfo(name, ip, id, password))
        }
    }
}
data class LoginUiState(
    val serverList: List<ServerHolderItem> = emptyList(),
    val loggedIn: Boolean = false
)