package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.viewmodel.logics.MainViewModelLogics
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
class MainViewModel @Inject constructor() : MainViewModelLogics() {

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

}