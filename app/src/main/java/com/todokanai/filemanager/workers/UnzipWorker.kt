package com.todokanai.filemanager.workers

import android.app.Notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnzipWorker(context: Context, params: WorkerParameters): CoroutineWorker(context,params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{

            Result.success()
        }catch (e:Exception){
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