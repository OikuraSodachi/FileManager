package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.tools.Requests
import com.todokanai.filemanager.tools.independent.exit_td
import com.todokanai.filemanager.tools.independent.getPhysicalStorages_td
import com.todokanai.filemanager.tools.independent.requestStorageManageAccess_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val workManager: WorkManager) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

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

    fun workRequestTest() {
        val request = Requests().copyRequest(emptyArray(), File(""))
        workManager.enqueue(request)
    }

}

data class MainActivityUiState(
    val temp: Int = 1
)