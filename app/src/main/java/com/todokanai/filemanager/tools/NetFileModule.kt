package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.NetFileModuleLogics
import com.todokanai.filemanager.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.IOException

class NetFileModule(
    private val dsRepo: DataStoreRepository,
    val defaultDirectory: String
) : NetFileModuleLogics(){

    private val ftpClient = FTPClient()
    val loggedIn = MutableStateFlow(false)

    suspend fun login() = withContext(Dispatchers.Default){
        ftpClient.run{
            connect(dsRepo.getServerIp(),21)
            if(login(dsRepo.getUserId(), dsRepo.getUserPassword())){
                loggedIn.value = true
            }
            enterLocalPassiveMode() // Passive Mode 사용
        }
    }

//    private val _currentFTPFile = MutableStateFlow<String>(defaultDirectory)
//    val currentFTPFile : StateFlow<String>
//        get() = _currentFTPFile

    override suspend fun ftpListFiles(): Array<FTPFile> {
        return listFilesInFtpDirectoryOriginal(
            dsRepo.getServerIp(),
            dsRepo.getUserId(),
            dsRepo.getUserPassword(),
            defaultDirectory
        )
    }

    private val _itemList = MutableStateFlow<Array<FTPFile>>(emptyArray())
    val itemList: Flow<Array<FTPFile>>
        get() = _itemList

//        get() = currentFTPFile.map {
//            try {
//                ftpClient.listFiles()
//            }catch (e:Exception){
//                emptyArray()
//            }
//        }.flowOn(
//            Dispatchers.Default
//        )

    override suspend fun setCurrentDirectory(directory: String) = withContext(Dispatchers.Default) {
        println("ftp: ${ftpClient.printWorkingDirectory()}")
        ftpClient.changeWorkingDirectory(directory)
        println("newFTP: ${ftpClient.printWorkingDirectory()}")
        val temp = ftpClient.listFiles(directory)
        println("temp: ${temp.map { it.name }}")
        _itemList.value = temp
    }

    suspend fun toParentDirectory(){
        ftpClient.changeToParentDirectory()
    }

    private suspend fun listFilesInFtpDirectory(
        server: String,
        username: String,
        password: String,
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
            result = ftpClient.listFiles()

            println("파일 목록 불러오기 성공: ${result.map{it.name}}")
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