package com.todokanai.filemanager.abstracts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

interface IOProgressAction{

    /** @param outputFileInProgress file that is currently in progress
     * @param bytesRead bytes read from input stream
     * @param bytesWritten total bytes written on [outputFileInProgress] **/
    suspend fun byteProgressCallback(outputFileInProgress:File, bytesRead:Long, bytesWritten:Long)

    /** start writing **/
    suspend fun doIOStreamWithProgress(
        inputStream:InputStream,
        outputFile: File,
        bufferSize: Int = 8192*100
    ) = withContext(Dispatchers.IO){
        val outputStream = outputFile.outputStream()
        var bytesWritten: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytesRead: Int

        while (inputStream.read(buffer).also { bytesRead = it } > 0) {
            outputStream.write(buffer, 0, bytesRead)
            bytesWritten += bytesRead
            byteProgressCallback(outputFile,bytesRead.toLong(),bytesWritten)
        }
        inputStream.close()
        outputStream.close()
    }
}