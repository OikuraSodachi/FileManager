package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.viewmodel.logics.LoginViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val serverRepo: ServerInfoRepository
) : LoginViewModelLogics() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            serverListFlow.collect {
                _uiState.update { currentState ->
                    currentState.copy(serverList = it)
                }
            }
        }
    }

    override val serverListFlow: Flow<List<ServerHolderItem>> =
        serverRepo.serverInfoFlow.map { infoList ->
            infoList.map {
                ServerHolderItem(
                    name = it.name,
                    id = it.no!!    // Todo: NPE 발생 가능성 확인 필요
                )
            }
        }

    override fun onServerClick(server: ServerHolderItem) {

    }

    override fun deleteServer(server: ServerHolderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.deleteByIndex(server.id)
        }
    }

    override fun saveServerInfo(name: String, ip: String, id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.insert(name, ip, id, password)
        }
    }

}

data class LoginUiState(
    val serverList: List<ServerHolderItem> = emptyList()
)