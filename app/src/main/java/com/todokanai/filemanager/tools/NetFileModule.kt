package com.todokanai.filemanager.tools

import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.IOException

class NetFileModule(
    private val dsRepo: DataStoreRepository
) {

    private val ftpClient = FTPClient()
    private val _loggedIn = MutableStateFlow(false)
    val loggedIn:StateFlow<Boolean>
        get() = _loggedIn

    suspend fun login() = withContext(Dispatchers.Default){
        ftpClient.run{
            connect(dsRepo.getServerIp(),21)
            if(login(dsRepo.getUserId(), dsRepo.getUserPassword())){
                _loggedIn.value = true
            }
            enterLocalPassiveMode() // Passive Mode 사용
        }
    }

    private val _currentDirectory = MutableStateFlow<String>("")
    val currentDirectory : StateFlow<String>
        get() = _currentDirectory

    suspend fun setCurrentDirectory(directory: String) = withContext(Dispatchers.Default) {
        if(isFileValid(directory)) {
            _currentDirectory.value = directory
        }
    }

    suspend fun toParentDirectory() = withContext(Dispatchers.Default){
        val parent = getParentAbsolutePath_td(currentDirectory.value)
        parent?.let{
            setCurrentDirectory(it)
        }
    }

    private fun isFileValid(directory: String):Boolean{
        var result = false

        val fileDetail = ftpClient.mlistFile(directory)
        println("detail: ${fileDetail.name}")
        result = true

        return result
    }

    suspend fun fileInfo(directory: String) : FTPFile = withContext(Dispatchers.Default){ ftpClient.mlistFile(directory) }

    suspend fun listFilesInFtpDirectory(
        server: String,
        username: String,
        password: String,
        directory: String,
        port: Int = 21
    ): Array<FTPFile> = withContext(Dispatchers.Default){
        var result = emptyArray<FTPFile>()
        try {
            // FTP 서버 연결
            ftpClient.run {
                connect(server, port)
                login(username, password)
                enterLocalPassiveMode() // Passive Mode 사용
            }

            // 특정 디렉토리 내 파일 목록 가져오기
            result = ftpClient.listFiles(directory)

            println("파일 목록 불러오기 성공: ${result.map{it.name}}")
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("파일 목록을 가져오는 중 오류 발생: ${ex.message}")
        }
        return@withContext result
    }

    private suspend fun listFilesInFtpDirectoryOriginal(
        server: String,
        username: String,
        password: String,
        remoteDirectory: String,
        port: Int = 21
    ): Array<FTPFile> = withContext(Dispatchers.Default){
        val ftpClient = FTPClient()
        var result = emptyArray<FTPFile>()
        try {
            // FTP 서버 연결
            ftpClient.run {
                connect(server, port)
                login(username, password)
                enterLocalPassiveMode() // Passive Mode 사용
            }

            // 특정 디렉토리 내 파일 목록 가져오기
            result = ftpClient.listFiles(remoteDirectory)

            println("파일 목록 불러오기 성공: $result")
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("파일 목록을 가져오는 중 오류 발생: ${ex.message}")
        } finally {
            try {
                ftpClient.logout()
                ftpClient.disconnect()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        return@withContext result
    }
}