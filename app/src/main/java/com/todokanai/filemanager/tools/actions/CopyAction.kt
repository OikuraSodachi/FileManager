package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.abstracts.IOProgressAction
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
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
    private val fileQuantity = getFileAndFoldersNumber_td(selectedFiles)
    private val fileSize = getTotalSize_td(selectedFiles)

    fun main() {
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
            if (file.isDirectory) {
                target.mkdirs()
                file.listFiles()?.forEach {
                    doIOStreamWithProgress(
                        inputStream = FileInputStream(it),
                        outputStream = FileOutputStream(target)
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
        println("byteProgress: ${bytesWritten}")
    }
}