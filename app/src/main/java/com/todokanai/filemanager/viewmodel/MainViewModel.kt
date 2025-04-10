package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.tools.independent.exit_td
import com.todokanai.filemanager.tools.independent.getPhysicalStorages_td
import com.todokanai.filemanager.tools.independent.requestStorageManageAccess_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    companion object {
        val myWorkerValue: Int = 1
    }

    fun prepareObjects(appContext: Context, activity: Activity) {
        myNoti = MyNotification(appContext)
    }

    /** 모든 파일 접근 권한 처리**/
    fun requestStorageManageAccess(
        activity: Activity,
        storages: MutableStateFlow<Array<File>> = Variables.storages
    ) {
        viewModelScope.launch {
            if (Environment.isExternalStorageManager()) {
                storages.value = getPhysicalStorages_td(activity)
            } else {
                requestStorageManageAccess_td(activity)
            }
        }
    }

    fun exit(activity: Activity) = exit_td(activity)

    /*
    fun exitTest(activity: Activity) = workerTest()

    /** work이 success를 반환해야 그 다음 work이 실행됨 **/
    private fun workerTest() {
      //  val workManager = WorkManager.getInstance(appContext)
        var continuation = workManager
            .beginUniqueWork(
                "MyWork",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(MyWorker::class.java)

            )

        /** Work의 Constraints 내용 **/
        val constraints = Constraints.Builder()
          //  .setRequiresCharging(true)
            .build()

        val testData = Data.Builder()
            .putBoolean("",true)
            .putInt(Constants.WORKER_TEST_SEED,1)
            .build()

        val workRequestTest = OneTimeWorkRequestBuilder<TestWorker>()
            .setConstraints(constraints)
            .setInputData(testData)
            //.addTag()
            .build()

        val notiTest = OneTimeWorkRequestBuilder<NotiWorker>()
            .setConstraints(constraints)
            .build()

        /** myWorker -> notiWorker -> testWorker 순 **/
        continuation = continuation.then(notiTest).then(workRequestTest)

        /** work 시작 **/
        continuation.enqueue()
    }

     */
}