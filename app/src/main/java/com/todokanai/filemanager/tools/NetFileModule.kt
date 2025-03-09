package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.BaseNetFileModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.IOException

class NetFileModule(
    val serverIp:String,
    val userId:String,
    val userPassword:String,
    val defaultDirectory:String
):BaseNetFileModule(defaultDirectory) {

    override suspend fun requestListFilesFromNet(directory: String): Array<FTPFile> {
        return listFilesInFtpDirectory(serverIp,userId,userPassword,directory)
    }

    override fun isFileValid(absolutePath: String): Boolean {
        return true
    }

    override val itemList: Flow<Array<FTPFile>>
        get() = currentDirectory.map {
            requestListFilesFromNet(it)
        }.flowOn(
            Dispatchers.Default
        )


    /** 아직 미검증 상태 **/
    private fun listFilesInFtpDirectory(
        server: String,
        username: String,
        password: String,
        remoteDirectory: String,
        port: Int = 21
    ): Array<FTPFile> {
        val ftpClient = FTPClient()
        var result = emptyArray<FTPFile>()
        try {
            // FTP 서버 연결
            ftpClient.run{
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

        return result
    }
}