package com.todokanai.filemanager.workers

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.todokanai.filemanager.R
import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CopyWorker(context: Context, params: WorkerParameters): CoroutineWorker(context,params)  {

    var progress : Int = 0

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{
            /** selected Files의 absolutePath 목록 **/
            val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!

            val targetDirectoryName = inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)!!

            println("Worker_stringArray: ${stringArray.toList()}")
            println("Worker_targetDirectory: $targetDirectoryName")

            val selectedFiles = stringArray.map{ File(it) }.toTypedArray()
            val targetDirectory = File(targetDirectoryName)

            val progress = 55
            setForeground(createForegroundInfo(progress))

            copyFiles_Recursive_td(selectedFiles,targetDirectory)


            Result.success()
        }catch(e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        //val out = ForegroundInfo(Constants.NOTIFICATION_CHANNEL_ID_WORK,notification())
        val progress = progress
        val out = createForegroundInfo(progress)
        return out
    }

    /** independent **/
    suspend fun copyFiles_Recursive_td(
        selected:Array<File>,
        targetDirectory:File,
        copyOption: CopyOption = StandardCopyOption.REPLACE_EXISTING
    ):Unit = withContext(Dispatchers.IO){
        for (file in selected) {
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                // Create the target directory
                target.mkdirs()
                // Copy the contents of the directory recursively
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target,copyOption)
            } else {
                // Copy the file
                Files.copy(file.toPath(), target.toPath(),)
            }
        }
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(progress: Int): ForegroundInfo {
        val id = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        val cancel = applicationContext.getString(R.string.notification_cancel)
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
          //  .setContentText(progress)
            .setProgress(100,progress,false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(0, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create a Notification channel
    }
}