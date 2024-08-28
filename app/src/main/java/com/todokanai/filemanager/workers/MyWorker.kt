package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.todokanai.filemanager.viewmodel.MainViewModel

class MyWorker(context:Context,params:WorkerParameters):Worker(context,params) {

    private val testValue = MainViewModel.myWorkerValue

    override fun doWork(): Result {

        return try{
            fun successed(value:Int):Boolean{
                return 10/value >0
            }
            val result = successed(testValue)
            if(result){
                println("MyWorker:Success")
                Result.success()
            } else{
                println("MyWorker: Failure")
                Result.failure()
            }


            Result.success()
        }catch (exception:Exception){
            println("MyWorker: Exception_Failure ${exception.message}")
            exception.printStackTrace()
            Result.failure()
        }
    }
}