package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.notifications.MyNotification

class NotiWorker(context: Context,params:WorkerParameters):Worker(context,params) {
    val noti by lazy{myNoti}
    override fun doWork(): Result {
        return try{

            noti.sendStringNotification("3","3")

            Result.success()
        } catch(exception:Exception){
            println("NotiWorker: Exception_Failure ${exception.message}")
            Result.failure()
        }

    }
}