package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import com.todokanai.filemanager.data.room.ServerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile

/** Todo: Dispatchers 주입식으로 바꾸기...? **/
class NetFileModule(defaultPath: String) : FileModuleLogics<FTPFile>(defaultPath) {

    private val ftpClient = FTPClient()

    private val currentServer = MutableStateFlow<ServerInfo?>(null)

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn = _loggedIn.asStateFlow()

    /** loggedIn 의 setter **/
    private fun setLoggedIn(value: Boolean) {
        _loggedIn.value = value
    }

    val itemList = combine(
        currentDirectory,
        currentServer
    ) { directory, server ->
        if (server != null) {
            getListFiles(directory).map {
                Pair(it, directory)
            }.toTypedArray()
        } else {
            emptyArray()
        }
    }

    suspend fun loginWrapper(serverInfo: ServerInfo) {
        val result = login(
            serverIp = serverInfo.ip,
            username = serverInfo.id,
            password = serverInfo.password,
            port = 21
        )

        if (result) {
            currentServer.value = serverInfo
            setLoggedIn(true)
        }               // 로그인 성공했을 경우 currentServer, loggedIn 값 변경.
    }

    /** @return true if login is successful. else false**/
    suspend fun login(
        serverIp: String,
        username: String,
        password: String,
        port: Int
    ): Boolean = withContext(Dispatchers.Default) {
        var result: Boolean
        ftpClient.run {
            connect(serverIp, port)
            result = login(username, password)
            enterLocalPassiveMode() // Passive Mode 사용
        }
        return@withContext result
    }

//    /** @throws IOException **/
//    suspend fun listFilesInFtpDirectory(directory: String): Array<FTPFile> = withContext(Dispatchers.Default) {
//        return@withContext ftpClient.listFiles(directory)
//    }

    override suspend fun getListFiles(directory: String): Array<FTPFile> =
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