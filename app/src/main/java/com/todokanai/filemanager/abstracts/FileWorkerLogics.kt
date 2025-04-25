package com.todokanai.filemanager.abstracts

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class FileWorkerLogics(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            workContent()
            Result.success()
        } catch (e: Exception) {
            onFailure(e)
            Result.failure()
        }
    }

    abstract suspend fun workContent()

    abstract fun onFailure(e: Exception)

}