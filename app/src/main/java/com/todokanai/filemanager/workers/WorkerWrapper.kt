package com.todokanai.filemanager.workers

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Constants
import java.io.File

class WorkerWrapper (private val workManager: WorkManager) {

    fun onConfirmCopy(selected:Array<File>, targetDirectory: File){
        val fileNames = selected.map { it.absolutePath }.toTypedArray()

        val inputData = Data.Builder()
            .putString(
                Constants.WORKER_KEY_TARGET_DIRECTORY,
                targetDirectory.absolutePath
            )
            .putStringArray(Constants.WORKER_KEY_COPY_FILE, fileNames)
            .build()

        val copyRequest = OneTimeWorkRequestBuilder<CopyWorker>()
            .setInputData(inputData)
            .build()

        var continuation = workManager
            .beginWith(copyRequest)

        continuation.enqueue()

    }


}