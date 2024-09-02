package com.todokanai.filemanager.workers

import android.app.Notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CopyWorker(context: Context, params: WorkerParameters): CoroutineWorker(context,params)  {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{
            /** selected Files의 absolutePath 목록 **/
            val stringArray =
                inputData.getStringArray(Constants.WORKER_KEY_COPY_FILE)

            val targetDirectoryName =
                inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)

            println("Worker_stringArray: ${stringArray!!.toList()}")
            println("Worker_targetDirectory: $targetDirectoryName")

            Result.success()
        }catch(e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val out = ForegroundInfo(Constants.NOTIFICATION_CHANNEL_ID_WORK,notification())
        return out
    }

    private fun notification(): Notification {
        TODO()
    }
}