package com.todokanai.filemanager.workers

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.todokanai.filemanager.myobjects.Constants
import java.io.File

class Requests {
    fun completedNotificationRequest(
        notiTitle:String = "Completed",
        notiText:String = "Work is done"
    ): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_TITLE,notiTitle)
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_MESSAGE,notiText)
            .putBoolean(Constants.WORKER_KEY_IS_SILENT,false)
            .build()
        val notiTest = OneTimeWorkRequestBuilder<NotiWorker>()
            .setInputData(inputData)
            .build()
        return notiTest
    }

    fun fileWork(
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

    fun deleteWork(
        requestType:OneTimeWorkRequest.Builder,
        selected: Array<File>
    ):OneTimeWorkRequest{
        /** selected:Array<File>을 전달에 File.absolutePath 대신 File.toUri().toString() 을 쓰는 방식도 검토해볼 것 **/
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .build()
        val request = requestType
            .setInputData(inputData)
            .build()
        return request
    }
}