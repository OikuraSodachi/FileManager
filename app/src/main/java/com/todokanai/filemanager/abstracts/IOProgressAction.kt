package com.todokanai.filemanager.abstracts

import java.io.InputStream
import java.io.OutputStream

interface IOProgressAction{

    fun byteProgressCallback(bytesWritten:Long)

    /** Gemini generated code
     * Todo: copy 작업 외에도 (zip 압축 등) 대응 가능하도록 ( abstraction ) 짤 것. **/
    fun doIOStreamWithProgress(inputStream:InputStream, outputStream: OutputStream, bufferSize: Int = 8192) {
        var bytesWritten: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } > 0) {
            outputStream.write(buffer, 0, bytesRead)
            bytesWritten += bytesRead
            byteProgressCallback(bytesWritten)
        }
        inputStream.close()
        outputStream.close()
    }
}