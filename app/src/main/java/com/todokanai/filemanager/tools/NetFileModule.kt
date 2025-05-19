package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject

/** Todo: Dispatchers 주입식으로 바꾸기...? **/
class NetFileModule @Inject constructor(val ftpClient: FTPClient, defaultPath: String) : FileModuleLogics<FTPFile>(defaultPath) {

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn = _loggedIn.asStateFlow()

    val itemList = currentDirectory.map{ directory ->
        try {
            ftpClient.listFiles(directory).map {
                Pair(it, directory)
            }.toTypedArray()
        }catch (e:Exception){
            emptyArray()
        }
    }

//    fun login(serverInfo: ServerInfo): Boolean {
//        val result = loginToFTPServer_td(
//            client = ftpClient,
//            serverIp = serverInfo.ip,
//            username = serverInfo.id,
//            password = serverInfo.password,
//            port = 21
//        )
//
//        if (result) {
//            currentServer.value = serverInfo
//            setLoggedIn(true)
//        }               // 로그인 성공했을 경우 currentServer, loggedIn 값 변경.
//        return result
//    }

    fun logout() {
        ftpClient.run {
            logout()
            disconnect()
        }       // Todo: 아직 미검증 상태
    }

    private suspend fun getListFiles(directory: String): Array<FTPFile> =
        withContext(Dispatchers.Default) {
            return@withContext ftpClient.listFiles(directory)
        }

    override suspend fun isDirectoryValid(directory: String): Boolean =
        withContext(Dispatchers.Default) {
            try {
                ftpClient.mlistFile(directory)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
}