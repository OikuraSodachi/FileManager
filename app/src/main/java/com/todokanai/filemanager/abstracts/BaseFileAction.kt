package com.todokanai.filemanager.abstracts

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

abstract class BaseFileAction(
    private val file: File
){
    /** 처리 완료된 byte 크기 **/
    private var bytesProcessed:Long = 0

    private val buffer = ByteArray(1024)


    suspend fun main(){


    }

    abstract suspend fun action(file: File)

    /** Gemini generated code
     * Todo: copy 작업 외에도 (zip 압축 등) 대응 가능하도록 ( abstraction ) 짤 것. **/
    fun copyFileWithProgress(sourceFile: File, destinationFile: File, byteProgressCallback:(Long)->Unit, bufferSize: Int = 8192) {

        var bytesCopied: Long = 0
        val inputStream = FileInputStream(sourceFile)
        val outputStream = FileOutputStream(destinationFile)

        val buffer = ByteArray(bufferSize)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } > 0) {
            outputStream.write(buffer, 0, bytesRead)
            bytesCopied += bytesRead
            byteProgressCallback(bytesCopied)
        }
        inputStream.close()
        outputStream.close()

    }
}