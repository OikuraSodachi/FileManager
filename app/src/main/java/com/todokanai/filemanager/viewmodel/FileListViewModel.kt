package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.actions.CopyAction
import com.todokanai.filemanager.tools.actions.DeleteAction
import com.todokanai.filemanager.tools.actions.MoveAction
import com.todokanai.filemanager.tools.actions.UnzipAction
import com.todokanai.filemanager.tools.actions.ZipAction
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

/** modeManager를 viewModel에서 완전히 제거해야 함 **/
@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository,val module:FileModule):ViewModel(){

    val notAccessible =  module.notAccessible
    val directoryList = module.dirTree

    val fileHolderList = combine(
        module.listFiles,
        dsRepo.sortBy
    ){
        listFiles,mode ->
        sortedFileList_td(listFiles,mode)
    }

    /** "Hot Flow" of [fileHolderList] **/
    val fileHolderListTest = combine(
        module.listFiles,
        dsRepo.sortBy
    ){ listFiles,mode ->
        println("listFiles: ${listFiles.map { it.name }}")
        println("mode: $mode")
        sortedFileList_td(listFiles,mode)
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly // SharingStarted.Eagerly / Lazily to make the flow hot
    )

    fun currentDirectory() : File {
        return module.currentPath.value
    }

    fun refreshFileList() = module.refreshListFiles()

    fun onDirectoryClick(directory:File) = module.updateCurrentPath(directory)

    fun onFileClick(context: Context, file: File) = module.onFileClick(context,file)

    fun onBackPressed() = module.onBackPressedCallback()

    fun copyWork(selected: Array<File>,targetDirectory: File){
        val action = CopyAction(selected,targetDirectory)
        action.start()
    }

    fun moveWork(selected: Array<File>,targetDirectory: File){
        MoveAction(selected,targetDirectory).start()
    }

    fun deleteWork(selected: Array<File>){
        DeleteAction(selected).start()
    }

    fun unzipWork(selected: File,targetDirectory: File){
        UnzipAction(selected,targetDirectory).start()
    }

    fun zipWork(selected:Array<File>,targetDirectory: File){
        ZipAction(selected, targetDirectory).start()
    }
}