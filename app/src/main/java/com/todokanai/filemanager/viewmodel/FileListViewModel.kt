package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Constants.BY_DEFAULT
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.myobjects.Objects.fileModule
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.workers.WorkerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

/** modeManager를 viewModel에서 완전히 제거해야 함 **/
@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository, private val workManager: WorkManager):ViewModel(){

    private val module = fileModule
    private val modeManager = Objects.modeManager
    val notAccessible =  module.notAccessible

    fun currentDirectory() : File {
        return module.currentPath.value
    }

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

    /** excute refresh by updating currentPath with same value
     *
     * 같은 값이 들어가서 livedata가 반응하지 않고 있음
     * **/
    fun refreshFileList(directory: File = module.currentPath.value) = module.updateCurrentPathSafe(directory)

    private fun updateDirectory(file:File) = module.updateCurrentPathSafe(file)

    fun onDefaultMode() = modeManager.onDefaultMode_new()
    fun onConfirmMoveMode() = modeManager.onConfirmMoveMode_new()
    fun onConfirmCopyMode() = modeManager.onConfirmCopyMode_new()

    //----------------------------------------------------
    //  viewModel에서 selectMode:StateFlow<Int>,selectedFiles : StateFlow<Array<File>> 정보를 걷어낼 목적으로 준비중인 구간

    fun onDirectoryClick_new(directory:File){
        module.updateCurrentPathSafe(directory)
    }

    fun onLongClick_new(file: File){
      //  modeManager.changeSelectMode(MULTI_SELECT_MODE)
        modeManager.onMultiSelectMode_new()
        modeManager.toggleToSelectedFiles(file)
    }

    fun onFileClick_new(context: Context, file: File) = module.onFileClick(context,file)


    fun onBackPressed_new(){
        module.currentPath.value.parentFile?.let {
            module.updateCurrentPathSafe(it)
        }
    }

    //------------------------------
    fun onConfirm_new(
        mode:Int,
        targetDirectory:File,
        selected:Array<File>
    ){
        when (mode) {
            CONFIRM_MODE_COPY -> {
                wrapper.onConfirmCopy(selected, targetDirectory)
            }

            CONFIRM_MODE_MOVE -> {
                wrapper.onConfirmMove(selected, targetDirectory)
            }

            CONFIRM_MODE_UNZIP -> {
                wrapper.onConfirmUnzip()
            }

            CONFIRM_MODE_UNZIP_HERE -> {

            }
        }
        //modeManager.changeSelectMode(DEFAULT_MODE)
        onDefaultMode()
    }

    fun onConfirmDelete_new(selected: Array<File>){
        //  val wrapper = WorkerWrapper(workManager,selected,targetDirectory).onConfirmDelete()
       // modeManager.changeSelectMode(DEFAULT_MODE)
        onDefaultMode()
    }
    //------------------------------------------
    // 동작별로 구분 방식 구간

    private val wrapper = WorkerWrapper(workManager)

    /** Copy 작업 시작 **/
    fun copyWork(selected: Array<File>,targetDirectory: File) = wrapper.onConfirmCopy(selected, targetDirectory)


    /** Move 작업 시작 **/
    fun moveWork(selected: Array<File>,targetDirectory: File){
        wrapper.onConfirmMove(selected,targetDirectory)
    }

    /** Delete 작업 시작 **/
    fun deleteWork(selected: Array<File>){
        wrapper.onConfirmDelete(selected)
    }

    fun unzipWork(selected: Array<File>,targetDirectory: File){

    }

}