package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import java.io.File

class MoveAction(
    val selectedFiles:Array<File>,
    val targetDirectory:File
):FileAction {
    var progress : Int = 0
    private val fileQuantity = selectedFiles.getFileAndFoldersNumber_td()
    private lateinit var currentFileInProcess : File

    override fun main() {
        moveFiles_Recursive_td(
            selected = selectedFiles,
            targetDirectory = targetDirectory,
            onProgress = {progressCallback()},
            onAlreadyExist = {},
            onDifferentStorage ={}
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification(currentFileInProcess.name,"$progress/$fileQuantity")
    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("moved $fileQuantity files","move complete")
    }

    /** independent **/
    fun moveFiles_Recursive_td(
        selected:Array<File>,
        targetDirectory: File,
        onProgress:(File)->Unit,
        onAlreadyExist:()->Unit,    // skip if left empty
        onDifferentStorage:()->Unit
    ):Unit{
        selected.forEach { file ->
            currentFileInProcess = file
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                // target.mkdirs()   // TODO: 생략해도 되는거 맞는지 검증할것
                moveFiles_Recursive_td(file.listFiles() ?: arrayOf(), target, onProgress, onAlreadyExist,onDifferentStorage)
                if(file.getRootDirectory() == target.getRootDirectory()) {
                    file.renameTo(target)
                } else{
                    file.copyTo(target)
                    file.delete()
                }
                onProgress(file)
            }
        }
    }

    private fun File.getRootDirectory():File{
        var result = this
        while(result.parentFile!=null){
            result = result.parentFile
        }
        return result
    }
}