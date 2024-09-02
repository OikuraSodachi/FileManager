package com.todokanai.filemanager.workers

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Constants
import java.io.File

class WorkerWrapper (private val workManager: WorkManager) {

    /** **/
    private fun completedNotificationRequest():OneTimeWorkRequest{
        val notiTitle = "Completed"
        val notiText = "Work is done"
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_TITLE,notiTitle)
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_MESSAGE,notiText)
            .build()
        val notiTest = OneTimeWorkRequestBuilder<NotiWorker>()
            .setInputData(inputData)
            .build()
        return notiTest
    }


    fun onConfirmCopy(selected:Array<File>, targetDirectory: File){
        val copyRequest = copyWorkRequest(selected, targetDirectory)
        val notiRequest = completedNotificationRequest()

        val continuation = workManager
            .beginWith(copyRequest)
            .then(notiRequest)
        continuation.enqueue()
    }

    private fun copyWorkRequest(selected:Array<File>, targetDirectory: File):OneTimeWorkRequest{
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

        return copyRequest
    }


}