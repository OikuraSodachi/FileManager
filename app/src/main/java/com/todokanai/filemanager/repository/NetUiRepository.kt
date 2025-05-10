package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.tools.NetFileModule
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class NetUiRepository @Inject constructor(
    val netModule: NetFileModule,
    val serverRepo: ServerInfoRepository
) {

    val currentServer = netModule.currentServer
    val currentDirectory = netModule.currentDirectory
    val serverListFlow = serverRepo.serverInfoFlow

    val itemList = combine(
        currentDirectory,
        currentServer
    ) { directory, server ->
        if (server != null) {       // 로그인 여부 체크
            netModule.getListFiles(directory).map {
                Pair(it, directory)
            }
        } else {
            emptyList()
        }
    }

//    val itemList = currentDirectory.map {
//        netModule.getListFiles(it)
//    }

    fun setCurrentServer(server: ServerInfo) = netModule.setCurrentServer(server)

}