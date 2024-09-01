package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.todokanai.filemanager.di.MyApplication.Companion.appContext
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Constants.BY_DEFAULT
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.myobjects.Objects.fileModule
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.workers.CopyWorker
import com.todokanai.filemanager.workers.NotiWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository):ViewModel(){

    private val module = fileModule
    val modeManager = Objects.modeManager
    val selectMode = modeManager.selectMode
    val notAccessible =  module.notAccessible

    val isMultiSelectMode = selectMode.map{ mode ->
        mode == MULTI_SELECT_MODE
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = false
    )

    val isDefaultMode = selectMode.map{ mode ->
        mode == DEFAULT_MODE
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = true
    )

    val selectedFiles = modeManager.selectedFiles

    private val sortMode = dsRepo.sortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = BY_DEFAULT
    )

    val directoryList = module.dirTree
    val fileHolderList = module.files.map { listFiles ->
        sortedFileList_td(listFiles,sortMode.value)
    }

    /** dummy data **/
    fun toggleToSelectedFiles(file: File) = modeManager.toggleToSelectedFiles(file)

    // ------------------------

    fun onDirectoryClick(directory:File,mode:Int = selectMode.value){
        if(mode != MULTI_SELECT_MODE) {
            module.updateCurrentPathSafe(directory)
        }
    }

    fun onClick(context: Context,file: File,mode:Int = selectMode.value){
        if(mode== MULTI_SELECT_MODE){
            toggleToSelectedFiles(file)
        }else{
            module.onFileClick(context,file)
        }
    }

    fun onLongClick(file: File,mode:Int = selectMode.value){
        if(mode== DEFAULT_MODE){
            toggleToSelectedFiles(file)
        } else{
            //empty
        }
    }

    fun onBackPressed(mode:Int = selectMode.value){
        if(mode == MULTI_SELECT_MODE){
            modeManager.changeSelectMode(DEFAULT_MODE)
        }else{
            module.currentPath.value.parentFile?.let {
                module.updateCurrentPathSafe(it)
            }
        }
    }

    fun refreshFileList(directory: File? = module.currentPath.value){
        directory?.let {
            module.updateCurrentPathSafe(it)
        }
    }

    private fun updateDirectory(file:File) = module.updateCurrentPathSafe(file)

    fun onDefaultMode() = modeManager.changeSelectMode(DEFAULT_MODE)
    fun onConfirmMoveMode() = modeManager.changeSelectMode(CONFIRM_MODE_MOVE)
    fun onConfirmCopyMode() = modeManager.changeSelectMode(CONFIRM_MODE_COPY)
    fun onMultiSelectMode() = modeManager.changeSelectMode(MULTI_SELECT_MODE)
    //------------------------------
    fun onConfirmTest(
        mode:Int = selectMode.value,
        targetDirectory:File = module.currentPath.value,
        selected:Array<File> = selectedFiles.value
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val workManager = WorkManager.getInstance(appContext)
            val fileNames : Array<String> = selected.map { it.absolutePath }.toTypedArray()
            println("stringArray: $fileNames")
            println("targetDirectory: ${targetDirectory.absolutePath}")

            when (mode) {
                CONFIRM_MODE_COPY -> {
                    val inputData = Data.Builder()
                        .putString(
                            Constants.WORKER_KEY_TARGET_DIRECTORY,
                            targetDirectory.absolutePath
                        )
                        .putStringArray(Constants.WORKER_KEY_COPY_FILE, fileNames)
                        .build()
                    val copyRequest = OneTimeWorkRequestBuilder<CopyWorker>()
                        .setInputData(inputData)
                        .build()

                    var continuation = workManager
                        //.beginWith(OneTimeWorkRequest.from(CopyWorker::class.java))
                        .beginWith(copyRequest)

                    continuation.enqueue()
                }

                CONFIRM_MODE_MOVE -> {

                }

                CONFIRM_MODE_UNZIP -> {

                }

                CONFIRM_MODE_UNZIP_HERE -> {

                }
            }
        }
    }
}