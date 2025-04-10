package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import java.io.File

class DeleteAction(
    val selectedFiles: Array<File>
) : FileAction {

    var progress: Int = 0
    private val fileQuantity = getFileAndFoldersNumber_td(selectedFiles)
    private lateinit var currentFileInProcess: File

    override fun main() {
        deleteRecursive_td(
            selected = selectedFiles,
            onProgress = { progressCallback() }
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification(currentFileInProcess.name, "deleted: $progress/$fileQuantity")
    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("delete completed", "deleted $progress files")
    }

    fun deleteRecursive_td(selected: Array<File>, onProgress: (File) -> Unit) {
        selected.forEach { file ->
            currentFileInProcess = file
            if (file.isDirectory) {
                file.listFiles()?.let {
                    deleteRecursive_td(it, onProgress)
                }
            }
            file.delete()
            onProgress(file)
        }
    }
}