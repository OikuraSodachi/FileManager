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

    /** Todo: try-catch 로 exception 처리 추가할 것 **/
    private fun isFileValid(directory: String): Boolean {
        var result = false

        val fileDetail = ftpClient.mlistFile(directory)
        println("detail: ${fileDetail.name}")
        result = true

        return result
    }

    suspend fun fileInfo(directory: String): FTPFile =
        withContext(Dispatchers.Default) { ftpClient.mlistFile(directory) }

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
            ftpClient.run {
                connect(server, port)
                login(username, password)
                enterLocalPassiveMode() // Passive Mode 사용
            }
            result = ftpClient.listFiles(directory)
            println("result: ${result.map { it.name }}")
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return@withContext result
    }
}