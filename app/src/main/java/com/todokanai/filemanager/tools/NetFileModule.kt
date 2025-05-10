package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import com.todokanai.filemanager.data.room.ServerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile

/** Todo: Dispatchers 주입식으로 바꾸기...? **/
class NetFileModule(defaultPath: String) : FileModuleLogics<FTPFile>(defaultPath) {

    private val ftpClient = FTPClient()

    private val _currentServer = MutableStateFlow<ServerInfo?>(null)
    val currentServer: Flow<ServerInfo?> = _currentServer.asStateFlow()

    fun setCurrentServer(server: ServerInfo) {
        _currentServer.value = server
    }

    suspend fun login(
        serverIp: String,
        username: String,
        password: String,
        port: Int
    ) : Boolean = withContext(Dispatchers.Default) {
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