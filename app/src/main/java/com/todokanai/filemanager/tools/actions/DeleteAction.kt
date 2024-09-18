package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteAction(
    val selectedFiles:Array<File>
):FileAction {

    var progress : Int = 0


    override fun main() {
        TODO("Not yet implemented")
    }

    override fun abort() {
        TODO("Not yet implemented")
    }

    override fun progressCallback(processedBytes: Long) {
        TODO("Not yet implemented")
    }

    override fun onComplete() {
        TODO("Not yet implemented")
    }

    /** independent **/
    fun deleteRecursively_td(
        file: File,
        onProgress:(File)->Unit
    ){
        try {
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    for (child in files) {
                        deleteRecursively_td(child, onProgress) // 재귀 호출
                    }
                }
            }
            file.delete()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}