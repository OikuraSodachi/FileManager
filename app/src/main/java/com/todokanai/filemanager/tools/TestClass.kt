package com.todokanai.filemanager.tools

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class TestClass {
    fun uploadFileToFtp(
        server: String, // server ip
        username: String,
        password: String,
        localFilePath: String, // 로컬 파일 경로
        remoteFilePath: String, // nas 파일 저장 경로
        port: Int = 21    // port (default = 21)
    ): Boolean {
        val ftpClient = FTPClient()
        return try {
            // FTP 서버 연결
            ftpClient.connect(server, port)
            ftpClient.login(username, password)
            ftpClient.enterLocalPassiveMode() // Passive Mode 사용 (방화벽 문제 방지)
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE) // 바이너리 파일 전송

            val localFile = File(localFilePath)
            FileInputStream(localFile).use { inputStream ->
                val uploaded = ftpClient.storeFile(remoteFilePath, inputStream)
                if (uploaded) {
                    println("파일 업로드 성공: $remoteFilePath")
                } else {
                    println("파일 업로드 실패")
                }
                uploaded
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("FTP 연결 또는 업로드 중 오류 발생: ${ex.message}")
            false
        } finally {
            try {
                ftpClient.logout()
                ftpClient.disconnect()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    /** 아직 미검증 상태 **/
    fun downloadFileFromFtp(
        server: String,
        username: String,
        password: String,
        remoteFilePath: String,
        localFilePath: String,
        port: Int = 21
    ): Boolean {
        val ftpClient = FTPClient()
        return try {
            // FTP 서버 연결
            ftpClient.connect(server, port)
            ftpClient.login(username, password)
            ftpClient.enterLocalPassiveMode() // Passive Mode 사용
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE) // 바이너리 파일 전송

            val localFile = File(localFilePath)
            FileOutputStream(localFile).use { outputStream ->
                val success = ftpClient.retrieveFile(remoteFilePath, outputStream)
                if (success) {
                    println("파일 다운로드 성공: $localFilePath")
                } else {
                    println("파일 다운로드 실패")
                }
                success
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            println("FTP 연결 또는 다운로드 중 오류 발생: ${ex.message}")
            false
        } finally {
            try {
                ftpClient.logout()
                ftpClient.disconnect()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}