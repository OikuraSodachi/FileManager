package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.ContextObjects
import com.todokanai.filemanager.myobjects.Objects.contextObjects
import com.todokanai.filemanager.myobjects.Objects.fileModel
import com.todokanai.filemanager.myobjects.Objects.mainActivityProvider
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.myobjects.Objects.packageName
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.providers.MainActivityProvider
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.getPhysicalStorages_td
import com.todokanai.filemanager.tools.independent.requestPermission_td
import com.todokanai.filemanager.tools.independent.requestStorageManageAccess_td
import com.todokanai.filemanager.viewmodel.submodel.FileListModel
import com.todokanai.filemanager.workers.MyWorker
import com.todokanai.filemanager.workers.NotiWorker
import com.todokanai.filemanager.workers.TestWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dsRepo:DataStoreRepository,val workManager: WorkManager):ViewModel() {

    companion object{
        val myWorkerValue: Int = 1
    }

    val currentDirectory by lazy{ fileModel.currentDirectory }

    fun invalidateCurrentDirectory(){
        fileModel.setCurrentDirectory(null)
    }

    fun prepareObjects(appContext: Context,activity: Activity){
        packageName = appContext.packageName
        fileModel = FileListModel()
      //  modeManager = SelectModeManager()
        contextObjects = ContextObjects(appContext)
        mainActivityProvider = MainActivityProvider(activity)
        myNoti = MyNotification(appContext)
    }

    /** 모든 파일 접근 권한 처리**/
    fun requestStorageManageAccess(activity: Activity,storages: MutableStateFlow<Array<File>> = Variables.storages) {
        viewModelScope.launch {
            if (Environment.isExternalStorageManager()) {
                storages.value = getPhysicalStorages_td(activity)
            } else {
                requestStorageManageAccess_td(activity)
            }
        }
    }

    fun allowNotification(activity: Activity){
        viewModelScope.launch {
            requestPermission_td(activity, permissions = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),{})
        }
    }

    fun onBackPressedOverride(activity: ComponentActivity){
        viewModelScope.launch {
            activity.onBackPressedDispatcher.addCallback {
                // disable back button by overriding with a empty callback
            }
        }
    }


    fun exitBtn() = workerTest()

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

        continuation = continuation.then(notiTest).then(workRequestTest)


        continuation.enqueue()
    }
}