package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.todokanai.filemanager.myobjects.Objects.fileModule
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.workers.Requests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import java.io.File
import javax.inject.Inject

/** modeManager를 viewModel에서 완전히 제거해야 함 **/
@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository, private val workManager: WorkManager):ViewModel(){

    private val request = Requests()
    private val module = fileModule
    val notAccessible =  module.notAccessible

    fun currentDirectory() : File {
        return module.currentPath.value
    }

    val directoryList = module.dirTree

    val fileHolderList = combine(
        module.listFiles,
        dsRepo.sortBy
    ){
        listFiles,mode ->
        sortedFileList_td(listFiles,mode)
    }

    fun refreshFileList() = module.refreshListFiles()

    fun onDirectoryClick(directory:File) = module.updateCurrentPath(directory)

    fun onFileClick(context: Context, file: File) = module.onFileClick(context,file)

    fun onBackPressed(){
        module.currentPath.value.parentFile?.let {
            module.updateCurrentPath(it)
        }
    }

    //------------------------------------------
    // 동작별로 구분 방식 구간

    /** Copy 작업 시작 **/
    fun copyWork(selected: Array<File>,targetDirectory: File){
        val copyRequest = request.copyRequest(selected, targetDirectory)
        val notiRequest = request.completedNotificationRequest()

        workManager
            .beginWith(copyRequest)
            .then(notiRequest)
            .enqueue()
    }


    /** Move 작업 시작 **/
    fun moveWork(selected: Array<File>,targetDirectory: File){
        val moveRequest = request.moveRequest(selected, targetDirectory)
        val notiRequest = request.completedNotificationRequest()
        workManager
            .beginWith(moveRequest)
            .then(notiRequest)
            .enqueue()
    }

    /** Delete 작업 시작 **/
    fun deleteWork(selected: Array<File>){
        val deleteRequest = request.deleteRequest(selected)
        val notiRequest = request.completedNotificationRequest()
        workManager
            .beginWith(deleteRequest)
            .then(notiRequest)
            .enqueue()
    }

    fun unzipWork(selected: Array<File>,targetDirectory: File){
        val unzipRequest = request.unzipRequest(selected, targetDirectory)
        val notiRequest = request.completedNotificationRequest()
        workManager
            .beginWith(unzipRequest)
            .then(notiRequest)
            .enqueue()
    }
}