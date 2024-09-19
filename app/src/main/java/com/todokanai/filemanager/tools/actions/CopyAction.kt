package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import java.io.File

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
            onProgress = { progressCallback() },
            onAlreadyExist = {}
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification(currentFileInProcess.name,"$progress/$fileQuantity")
    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("copied $fileQuantity files","copy complete")
    }

    /** independent **/
    fun copyFiles_Recursive_td(
        selected:Array<File>,
        targetDirectory: File,
        onProgress:(File)->Unit,
        onAlreadyExist:()->Unit
    ):Unit{
        selected.forEach{ file ->
            currentFileInProcess = file
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target,onProgress,onAlreadyExist)
                if(target.exists()){
                    onAlreadyExist()
                }else{
                    file.copyTo(target)
                }
                onProgress(file)
            }
        }
    }
}