package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.abstracts.FileWorkerLogics
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CopyWorker(val context: Context, params: WorkerParameters) : FileWorkerLogics(context, params) {

    var progress: Int = 0

    /** selected Files의 absolutePath 목록 **/
    private val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!
    private val targetDirectoryName = inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)!!
    private val selectedFiles = stringArray.map { File(it) }.toTypedArray()
    private val targetDirectory = File(targetDirectoryName)

    val fileQuantity = getFileAndFoldersNumber_td(selectedFiles)

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(context,fileQuantity, progress)
    }

    override suspend fun workContent() {
        copyFiles_Recursive_td(
            selected = selectedFiles,
            targetDirectory = targetDirectory,
            onProgress = {
                progress++
                //getForegroundInfo()
                myNoti.sendSilentNotification(context,it.name, "$progress/$fileQuantity")
            }
        )
    }

    override fun onFailure(e: Exception) {
        e.printStackTrace()
    }

    /** independent **/
    suspend fun copyFiles_Recursive_td(
        selected: Array<File>,
        targetDirectory: File,
        onProgress: (File) -> Unit,
        copyOption: CopyOption = StandardCopyOption.REPLACE_EXISTING
    ): Unit = withContext(Dispatchers.IO) {
        for (file in selected) {
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                // Create the target directory
                target.mkdirs()
                onProgress(file)
                // Copy the contents of the directory recursively
                copyFiles_Recursive_td(
                    file.listFiles() ?: arrayOf(),
                    target,
                    onProgress,
                    copyOption
                )
            } else {
                // Copy the file
                Files.copy(file.toPath(), target.toPath())
                onProgress(file)
            }
        }
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(context: Context,max: Int, progress: Int): ForegroundInfo {
        println("max:$max, progress:$progress")
        val test = myNoti.ongoingNotiTest(context)
            .setProgress(max, progress, false)
            .setContentText("$progress/$max")

        return ForegroundInfo(0, test.build())
    }
}