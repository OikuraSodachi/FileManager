package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.tools.independent.NetFileModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NetUiRepository @Inject constructor(
    val netModule: NetFileModule,
    val serverRepo: ServerInfoRepository
) {
    /** Todo: 이 값이 UiRepository 에 있는 게 적절한지? (UI 로 직접 올라가지 않는 상황임 )**/
    private val _currentServer = MutableStateFlow<ServerInfo?>(null)
    val currentServer: Flow<ServerInfo?> = _currentServer.asStateFlow()

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn = _loggedIn.asStateFlow()

    private val currentDirectory = netModule.currentDirectory
    val dirTree = currentDirectory.map {
        convertToDirTree(it)
    }
    val itemList: Flow<List<FileHolderItem>> =
        combine(
            netModule.currentDirectory,
            currentServer
        ) { directory, server ->

            val result =
                if (server != null) {
                    netModule.listFilesInFtpDirectory(
                        server.ip,
                        server.id,
                        server.password,
                        directory
                    ).map {
                        FileHolderItem.fromFTPFile(it, directory)
                    }
                } else {
                    emptyList()
                }
            result
        }

    val serverListFlow: Flow<List<ServerHolderItem>> =
        serverRepo.serverInfoFlow.map { infoList ->
            infoList.map {
                ServerHolderItem(
                    name = it.name,
                    id = it.no!!    // Todo: NPE 발생 가능성 확인 필요
                )
            }
        }

    fun setCurrentServer(server: ServerInfo) {
        _currentServer.value = server
    }

    fun setLoggedIn(value: Boolean) {
        _loggedIn.value = value
    }

    private fun convertToDirTree(absolutePath: String): List<DirectoryHolderItem> {
        val result = mutableListOf<DirectoryHolderItem>()
        var target = absolutePath
        while (target != "") {
            result.add(
                DirectoryHolderItem(
                    name = getLastSegment(target),
                    absolutePath = target
                )
            )
            target = testRegex(target)
        }
        result.reverse()
        return result
    }

    private fun testRegex(path: String): String {
        val regex = """(.*)/[^/]*$""".toRegex()
        val matchResult = regex.find(path)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun getLastSegment(path: String): String {
        val regex = """[^/]*$""".toRegex()
        val matchResult = regex.find(path)
        return matchResult?.value ?: ""
    }

}