package com.todokanai.filemanager.tools

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.workers.CopyWorker
import java.io.File

class Requests {

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

}