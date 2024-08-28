package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TestWorker(context:Context,params:WorkerParameters):CoroutineWorker(context,params) {

    val noti = myNoti


    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try{

            println("testData = ${inputData.getBoolean(Constants.WORKER_TEST_SEED,false)}")
            fun successed(value:Int):Boolean{
                return 10/value >0
            }

            val out = successed(inputData.getInt(Constants.WORKER_TEST_SEED,0))

            if(out){

                delay(10)
                println("TestWorker: Success")

                noti.sendStringNotification("test","success")

                Result.success()
            } else{
                println("TestWorker: Failure")
                noti.sendStringNotification("test","fail")

                Result.failure()
            }
        } catch(exception:Exception){
            println("TestWorker: Exception_Failure  ${exception.message}")
            noti.sendStringNotification("test","fail_exception")

            Result.failure()
        }
    }

}