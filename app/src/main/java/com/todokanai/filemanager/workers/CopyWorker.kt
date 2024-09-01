package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CopyWorker(context: Context, params: WorkerParameters): Worker(context,params)  {

    override fun doWork(): Result{
        return try{
            /** selected Files의 absolutePath 목록 **/
            val stringArray =
                inputData.getStringArray(Constants.WORKER_KEY_COPY_FILE)

            val targetDirectoryName =
                inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)

            println("Worker_stringArray: $stringArray")
            println("Worker_targetDirectory: $targetDirectoryName")

            Result.success()
        }catch(e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}