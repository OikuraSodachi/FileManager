package com.todokanai.filemanager.tools.independent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile

/** Todo: FileModuleLogics 적용하기, Dispatchers 주입식으로 바꾸기 **/
class NetFileModule {

    private val ftpClient = FTPClient()

    suspend fun login(
        serverIp: String,
        username: String,
        password: String,
        port: Int
    ) = withContext(Dispatchers.Default) {
        ftpClient.run {
            connect(serverIp, port)
            login(username, password)
            enterLocalPassiveMode() // Passive Mode 사용
        }
    }

    private val _currentDirectory = MutableStateFlow<String>("")
    val currentDirectory: StateFlow<String>
        get() = _currentDirectory

    suspend fun setCurrentDirectory(directory: String) = withContext(Dispatchers.Default) {
        if (isFileValid(directory)) {
            _currentDirectory.value = directory
        }
    }

    /** @return true if file is valid. else false **/
    private fun isFileValid(directory: String): Boolean {
        try {
            ftpClient.mlistFile(directory)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /** @throws IOException **/
    suspend fun listFilesInFtpDirectory(directory: String): Array<FTPFile> = withContext(Dispatchers.Default) {
        return@withContext ftpClient.listFiles(directory)
    }

    suspend fun downloadFTPFiles(ftpFiles: Array<FTPFile>) = withContext(Dispatchers.IO) {
    }
}