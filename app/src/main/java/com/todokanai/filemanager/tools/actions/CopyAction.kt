package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.abstracts.IOProgressAction
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/** @param selectedFiles selected files
 *  @param targetDirectory target directory **/
class CopyAction(
    val selectedFiles: Array<File>,
    val targetDirectory: File
):IOProgressAction{
    private val fileSize = getTotalSize_td(selectedFiles)
    private var bytesDone:Long = 0
    fun start() {
        println("CopyAction - start")
        CoroutineScope(Dispatchers.IO).launch {
            copyFiles_Recursive_td(
                selected = selectedFiles,
                targetDirectory = targetDirectory
            )
        }
    }

    fun copyFiles_Recursive_td(
        selected: Array<File>,
        targetDirectory: File
    ) {
        selected.forEach { file ->
            val target = targetDirectory.resolve(file.name)
            println("target: ${target.absolutePath}")
            if (file.isDirectory) {
                target.mkdirs()
                file.listFiles()?.forEach {
                    doIOStreamWithProgress(
                        inputStream = FileInputStream(it),
                        outputStream = FileOutputStream(target.resolve(it.name))
                    )
                }
            } else {
                doIOStreamWithProgress(
                    inputStream = FileInputStream(file),
                    outputStream = FileOutputStream(target)
                )
            }
        }
    }

    override fun byteProgressCallback(bytesWritten: Long) {
        bytesDone = bytesDone+bytesWritten      // Todo : progress 상황 로직 미완성
        println("byteProgress: ${ 100*(bytesWritten/fileSize).toInt() } %")
    }
}