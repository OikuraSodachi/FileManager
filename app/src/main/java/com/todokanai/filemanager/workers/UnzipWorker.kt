package com.todokanai.filemanager.workers

import android.app.Notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UnzipWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    var progress: Int = 0

    private val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!
    private val selectedFiles = stringArray.map { File(it) }.toTypedArray()

    private val string = inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)!!
    private val targetDirectory = File(string)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val out = ForegroundInfo(Constants.NOTIFICATION_CHANNEL_ID_WORK, notification())
        return out
    }

    private fun notification(): Notification {
        TODO()
    }
}