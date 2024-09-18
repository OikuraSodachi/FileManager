package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import java.io.File

class DeleteAction(
    val selectedFiles:Array<File>
):FileAction {

    var progress : Int = 0
    private val fileQuantity = selectedFiles.getFileAndFoldersNumber_td()
    private lateinit var currentFileInProcess : File

    override fun main() {
        deleteRecursively_td(
            selected = selectedFiles,
            onProgress = { progressCallback() }
        )
    }

    override fun abort() {

    }

    override fun progressCallback() {
        progress++
        myNoti.sendSilentNotification("titleText","deleted: $progress/$fileQuantity")
    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("completed","delete completed")
    }

    /** independent **/
    fun deleteRecursively_td(
        selected:Array<File>,
        onProgress:(File)->Unit
    ){
        selected.forEach{ file ->
            currentFileInProcess = file
            if (file.isDirectory) {

                file.listFiles()?.let{
                    deleteRecursively_td(it, onProgress) // 재귀 호출
                }
                /*
                if (files != null) {
                        deleteRecursively_td(file.listFiles() ?: arrayOf(), onProgress) // 재귀 호출
                }
                 */
            }
            file.delete()
        }
    }
}