package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CopyAction(
    val selectedFiles:Array<File>,
    val targetDirectory: File
) : FileAction {
    var progress : Int = 0
    private val fileQuantity = getFileAndFoldersNumber_td(selectedFiles)
    private lateinit var currentFileInProcess : File

    override fun main() {
        copyFiles_Recursive_td(
            selected = selectedFiles,
            targetDirectory = targetDirectory,
            onProgress = { progressCallback() }
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification(
            title = currentFileInProcess.name,
            message = "$progress/$fileQuantity"
        )
    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("copied $progress files","copy complete")
    }

    fun copyFiles_Recursive_td(selected: Array<File>, targetDirectory: File,onProgress: (File) -> Unit) {
        selected.forEach{ file ->
            val target = targetDirectory.resolve(file.name)
            currentFileInProcess = target
            if (file.isDirectory) {
                target.mkdirs()
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target,onProgress)
            } else {
                Files.copy(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            onProgress(file)
        }
    }
}