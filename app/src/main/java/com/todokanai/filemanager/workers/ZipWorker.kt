package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.TestClass
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ZipWorker(context: Context, params: WorkerParameters): CoroutineWorker(context,params) {

    var bytesDone : Long = 0L

    val test = TestClass()

    private val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!
    private val targetDirectoryName = inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)!!
    private val selectedFiles = stringArray.map{ File(it) }.toTypedArray()
    private val targetDirectory = File(targetDirectoryName)

    val totalSize = selectedFiles.getTotalSize_td()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{
            println("started")
            println("selected: ${selectedFiles.map{it.name}}")
            println("targetZipFile: ${targetDirectory.absolutePath}")

            fun callback(processed:Long){
                val progress = (processed*100/totalSize).toInt()
                println("progress: $progress")
                println("bytes: $processed")
             //   println("check: ${processed}")

            }

            test.zipFilesWithProgress(
                selected = selectedFiles,
                target = targetDirectory,
                progressCallback = {callback(it)}
                )


            Result.success()
        }catch (e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(totalSize,bytesDone)
    }

    private fun createForegroundInfo(max:Long,progress: Long): ForegroundInfo {
      //  println("max:$max, progress:$progress")
        val test = myNoti.ongoingNotiTest
            .setProgress(max.toInt(),progress.toInt(),false)
            .setContentText("$progress/$max")

        return ForegroundInfo(0,test.build())
    }

}