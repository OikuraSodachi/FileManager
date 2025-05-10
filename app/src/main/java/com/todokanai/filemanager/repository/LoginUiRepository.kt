package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.room.ServerInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LoginUiRepository @Inject constructor(
    val serverRepo:ServerInfoRepository
) {

    /** Todo: 이 값이 UiRepository 에 있는 게 적절한지? (UI 로 직접 collect 하지 않는 상황임 )**/
    private val _currentServer = MutableStateFlow<ServerInfo?>(null)
    val currentServer: Flow<ServerInfo?> = _currentServer.asStateFlow()

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn = _loggedIn.asStateFlow()

    val serverListFlow = serverRepo.serverInfoFlow

    fun setCurrentServer(server: ServerInfo) {
        _currentServer.value = server
    }

    fun setLoggedIn(value: Boolean) {
        _loggedIn.value = value
    }

}