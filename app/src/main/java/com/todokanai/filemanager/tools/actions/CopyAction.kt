package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.abstracts.IOProgressAction
import com.todokanai.filemanager.tools.independent.getFileNumber_td
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream

/** @param selectedFiles selected files
 *  @param targetDirectory target directory **/
class CopyAction(
    val selectedFiles: Array<File>,
    val targetDirectory: File
):IOProgressAction{
    private val fileSize = getTotalSize_td(selectedFiles)
    private val fileQuantity = getFileNumber_td(selectedFiles)

    private lateinit var currentSourceFile : File
    private var bytesDone:Long = 0
    private var filesDone:Int = 0

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            selectedFiles.forEach { file ->
                val target = targetDirectory.resolve(file.name)
                if (file.isDirectory) {
                    target.mkdirs()
                    file.listFiles()?.forEach {
                        doIOStreamWithProgress_Wrapper(it,target.resolve(it.name))
                    }
                } else {
                    doIOStreamWithProgress_Wrapper(file,target)
                }
            }
        }
    }

    private suspend fun doIOStreamWithProgress_Wrapper(sourceFile: File, targetFile:File){
        currentSourceFile = sourceFile
        doIOStreamWithProgress(
            inputStream = FileInputStream(currentSourceFile),
            outputFile = targetFile
        )
        filesDone += 1
    }

    override suspend fun byteProgressCallback(outputFileInProgress: File, bytesRead: Long, bytesWritten: Long) {
        bytesDone += bytesRead
       // val temp = readableFileSize_td(bytesDone)
      //  println("${fileInProgress.name} : ${ (100*bytesDone)/fileSize } %")

        val totalNumber = fileQuantity
        val currentNumber = filesDone

        val totalProgress = (100*bytesDone)/fileSize
        val currentFileProgress = (100*bytesWritten)/currentSourceFile.length()
     //   println("bytesWritten: ${readableFileSize_td(bytesWritten)}, size: ${readableFileSize_td(outputFileInProgress.length())}")

        println("number: $currentNumber/$totalNumber, fileProgress: $currentFileProgress%, progress: $totalProgress%")
    }
}