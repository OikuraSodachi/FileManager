package com.todokanai.filemanager.workers

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Constants
import java.io.File

class WorkerWrapper (private val workManager: WorkManager/*, private val selected: Array<File>, private val targetDirectory: File*/) {

    fun onConfirmCopy(selected: Array<File>, targetDirectory: File){
        val copyRequest = fileWorkBuilder(OneTimeWorkRequestBuilder<CopyWorker>(),selected, targetDirectory)
        val notiRequest = completedNotificationRequest()

        val continuation = workManager
            .beginWith(copyRequest)
            .then(notiRequest)
        continuation.enqueue()
    }

    fun onConfirmMove(selected: Array<File>, targetDirectory: File){
        val moveRequest = fileWorkBuilder(OneTimeWorkRequestBuilder<MoveWorker>(),selected, targetDirectory)
        val notiRequest = completedNotificationRequest()
        val continuation = workManager
            .beginWith(moveRequest)
            .then(notiRequest)
        continuation.enqueue()

    }

    fun onConfirmUnzip(){

    }

    fun onConfirmDelete(selected: Array<File>){


    }


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

    private fun fileWorkBuilder(
        requestType:OneTimeWorkRequest.Builder,
        selected: Array<File>,
        targetDirectory: File
    ):OneTimeWorkRequest{
        /** selected:Array<File>을 전달에 File.absolutePath 대신 File.toUri().toString() 을 쓰는 방식도 검토해볼 것 **/
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_TARGET_DIRECTORY, targetDirectory.absolutePath)
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .build()
        val request = requestType
            .setInputData(inputData)
            .build()
        return request
    }
}