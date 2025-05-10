package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.tools.NetFileModule
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NetUiRepository @Inject constructor(
    val netModule: NetFileModule,
    val serverRepo: ServerInfoRepository
) {

    val currentDirectory = netModule.currentDirectory
    val serverListFlow = serverRepo.serverInfoFlow.map { serverInfo ->
        serverInfo.map{
            ServerHolderItem(it.id, it.no!!) // Todo: NPE 발생 가능성 확인 필요
        }
    }

    val itemList = netModule.itemList.map {
        it.toList()
    }

    fun setCurrentServer(server: ServerInfo) = netModule.setCurrentServer(server)

}