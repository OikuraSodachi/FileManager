package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CopyAction(
    val selectedFiles:Array<File>,
    val targetDirectory: File
) : FileAction {
    var progress : Int = 0
    private val fileQuantity = selectedFiles.getFileAndFoldersNumber_td()
    private lateinit var currentFileInProcess : File

    override fun main() {
        copyFiles_Recursive_td(
            selected = selectedFiles,
            targetDirectory = targetDirectory,
            onProgress = {
                progress++
                myNoti.sendSilentNotification(it.name,"$progress/$fileQuantity")
            }
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification(currentFileInProcess.name,"$progress/$fileQuantity")
    }

    override fun onComplete() {
        println("copy complete")
        myNoti.sendCompletedNotification("copied $fileQuantity files","copy complete")
    }

    /** independent **/
    fun copyFiles_Recursive_td(
        selected:Array<File>,
        targetDirectory: File,
        onProgress:(File)->Unit,
        copyOption: CopyOption = StandardCopyOption.REPLACE_EXISTING
    ):Unit{
        for (file in selected) {
            currentFileInProcess = file
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                // Create the target directory
                target.mkdirs()
                onProgress(file)
                // Copy the contents of the directory recursively
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target,onProgress, copyOption)
            } else {
                // Copy the file
                Files.copy(file.toPath(), target.toPath(),)
                onProgress(file)
            }
        }
    }
}