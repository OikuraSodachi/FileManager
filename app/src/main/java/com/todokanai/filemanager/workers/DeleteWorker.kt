package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteWorker(context: Context, params: WorkerParameters): CoroutineWorker(context,params) {

    var progress : Int = 0

    private val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!
    private val selectedFiles = stringArray.map{ File(it) }.toTypedArray()
    val fileQuantity = selectedFiles.getFileAndFoldersNumber_td()


    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{
            //println("Worker_stringArray: ${stringArray.toList()}")
            selectedFiles.forEach { file ->
                deleteRecursively_td(
                    file = file,
                    onProgress = {
                        progress++
                        myNoti.sendSilentNotification("titleText","deleted: $progress/$fileQuantity")
                    }
                )
            }


            Result.success()
        }catch(e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
    /** independent **/
    suspend fun deleteRecursively_td(
        file: File,
        onProgress:(File)->Unit
    ):Unit = withContext(Dispatchers.IO){
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