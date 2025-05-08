package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile

/** Todo: FileModuleLogics 적용하기, Dispatchers 주입식으로 바꾸기 **/
class NetFileModule : FileModuleLogics<FTPFile>("") {

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

    /** @throws IOException **/
    suspend fun listFilesInFtpDirectory(directory: String): Array<FTPFile> = withContext(Dispatchers.Default) {
        return@withContext ftpClient.listFiles(directory)
    }

    override suspend fun getListFiles(directory: String): Array<FTPFile> {
        return ftpClient.listFiles(directory)
    }

    override suspend fun isDirectoryValid(directory: String): Boolean {
        try {
            ftpClient.mlistFile(directory)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}