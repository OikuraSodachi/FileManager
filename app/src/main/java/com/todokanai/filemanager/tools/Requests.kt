package com.todokanai.filemanager.tools

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.workers.DeleteWorker
import com.todokanai.filemanager.workers.MoveWorker
import com.todokanai.filemanager.workers.NotiWorker
import com.todokanai.filemanager.workers.UnzipWorker
import java.io.File

class Requests {

    /*
    fun copyRequest(
        selected: Array<File>,
        targetDirectory: File
    ): OneTimeWorkRequest {
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_TARGET_DIRECTORY, targetDirectory.absolutePath)
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .build()
        val request = OneTimeWorkRequestBuilder<CopyWorker>()
            .setInputData(inputData)
            .build()
        return request
    }

     */

    fun moveRequest(
        selected: Array<File>,
        targetDirectory: File
    ):OneTimeWorkRequest{
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_TARGET_DIRECTORY, targetDirectory.absolutePath)
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .build()
        val request = OneTimeWorkRequestBuilder<MoveWorker>()
            .setInputData(inputData)
            .build()
        return request
    }

    fun deleteRequest(selected: Array<File>):OneTimeWorkRequest{
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .build()
        val request = OneTimeWorkRequestBuilder<DeleteWorker>()
            .setInputData(inputData)
            .build()
        return request
    }

    fun unzipRequest(selected: Array<File>,targetDirectory: File):OneTimeWorkRequest{
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val inputData = Data.Builder()
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .putString(Constants.WORKER_KEY_TARGET_DIRECTORY,targetDirectory.absolutePath)
            .build()
        val request = OneTimeWorkRequestBuilder<UnzipWorker>()
            .setInputData(inputData)
            .build()
        return request
    }

    /*
    fun zipRequest(
        selected: Array<File>,
        targetDirectory: File
    ):OneTimeWorkRequest{
        val fileNames = selected.map { it.absolutePath }.toTypedArray()
        val target = targetDirectory.absolutePath
        val inputData = Data.Builder()
            .putStringArray(Constants.WORKER_KEY_SELECTED_FILES, fileNames)
            .putString(Constants.WORKER_KEY_TARGET_DIRECTORY,target)
            .build()
        val request = OneTimeWorkRequestBuilder<ZipWorker>()
            .setInputData(inputData)
            .build()
        return request
    }

     */

    fun completedNotificationRequest(
        notiTitle:String = "Completed",
        notiText:String = "Work is done"
    ): OneTimeWorkRequest {
        val inputData = Data.Builder()
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_TITLE,notiTitle)
            .putString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_MESSAGE,notiText)
            .putBoolean(Constants.WORKER_KEY_IS_SILENT,false)
            .build()
        val noti = OneTimeWorkRequestBuilder<NotiWorker>()
            .setInputData(inputData)
            .build()
        return noti
    }
}