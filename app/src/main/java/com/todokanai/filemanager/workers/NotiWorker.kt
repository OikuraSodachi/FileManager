package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti

class NotiWorker(context: Context,params:WorkerParameters):Worker(context,params) {
    val noti by lazy{myNoti}
    override fun doWork(): Result {
        return try{

            val isSilent = inputData.getBoolean(Constants.WORKER_KEY_IS_SILENT,true)
            val title = inputData.getString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_TITLE)
            val message = inputData.getString(Constants.WORKER_KEY_NOTIFICATION_COMPLETE_MESSAGE)


            if(isSilent) {
                noti.sendStringNotification(title, message)
            }else{
                noti.sendCompletedNotification(title, message)
            }

            Result.success()
        } catch(exception:Exception){
            println("NotiWorker: Exception_Failure ${exception.message}")
            Result.failure()
        }

    }
}