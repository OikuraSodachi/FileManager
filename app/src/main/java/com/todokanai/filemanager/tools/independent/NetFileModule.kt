package com.todokanai.filemanager.tools.independent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.IOException

class NetFileModule {

    private val ftpClient = FTPClient()

    suspend fun login(
        server: String,
        username: String,
        password: String,
        port: Int
    ) = withContext(Dispatchers.Default) {
        ftpClient.run {
            connect(server, port)
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

    suspend fun listFilesInFtpDirectory(
        server: String,
        username: String,
        password: String,
        directory: String,
        port: Int = 21
    ): Array<FTPFile> = withContext(Dispatchers.Default) {
        var result = emptyArray<FTPFile>()
        try {
            // FTP 서버 연결
            login(server, username, password, port)
            result = ftpClient.listFiles(directory)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return@withContext result
    }

    suspend fun downloadFTPFiles(ftpFiles: Array<FTPFile>) = withContext(Dispatchers.IO) {
    }
}