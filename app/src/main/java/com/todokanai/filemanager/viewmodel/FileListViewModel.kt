package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Constants.BY_DEFAULT
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.myobjects.Objects.fileModel
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.dirTree_td
import com.todokanai.filemanager.tools.independent.isAccessible_td
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository):ViewModel(){

    private val model by lazy{fileModel}
    private val modeManager by lazy{ Objects.modeManager}
    private val currentDirectory by lazy{model.currentDirectory }

    val notAccessible by lazy{ model.notAccessible}
    val isEmpty by lazy{model.isEmpty}        // is directory.listFiles() empty

    val selectMode by lazy{modeManager.selectMode}

    val selectedFiles by lazy{modeManager.selectedFiles}

    val sortMode = dsRepo.sortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = BY_DEFAULT
    )

    val directoryList by lazy{
        currentDirectory.map{ file ->
            file?.let { dirTree_td(it) }
        }
    }

    val fileHolderListNew by lazy{
        model.listFiles.map { listFiles ->
            sortedFileList_td(listFiles?: emptyArray(),sortMode.value)
        }
    }

    // ------------------------

    fun onDirectoryClick(directory:File,mode:Int = selectMode.value){
        viewModelScope.launch {
            if(mode != MULTI_SELECT_MODE) {
                if (directory.isAccessible_td()) {        // 접근 가능여부 체크
                    updateDirectory(directory)
                }
            }
        }
    }

    fun onClick(context: Context,file: File,mode:Int = selectMode.value){
        when (mode) {
            DEFAULT_MODE -> {
                viewModelScope.launch {
                    if (file.isDirectory) {
                        updateDirectory(file)
                    } else {
                        model.openFile(context,file)
                    }
                }
            }
            MULTI_SELECT_MODE -> {
                viewModelScope.launch {
                    modeManager.setSelectedFiles(file)
                }
            }

            CONFIRM_MODE_COPY -> {
                viewModelScope.launch {
                    if(file.isDirectory){
                        updateDirectory(file)
                    } else{
                        // empty
                    }
                }
            }

            CONFIRM_MODE_MOVE -> {
                viewModelScope.launch {
                    if(file.isDirectory){
                        updateDirectory(file)
                    } else{
                        // empty
                    }
                }
            }

            CONFIRM_MODE_UNZIP -> {
                viewModelScope.launch {
                    if(file.isDirectory){
                        updateDirectory(file)
                    } else{
                        // empty
                    }
                }
            }
            CONFIRM_MODE_UNZIP_HERE -> {
                viewModelScope.launch {
                    if(file.isDirectory){
                        updateDirectory(file)
                    } else{
                        // empty
                    }
                }
            }
        }
    }

    fun onLongClick(file: File,mode:Int = selectMode.value){
        when (mode) {
            DEFAULT_MODE -> {
                modeManager.run{
                    setSelectedFiles(file)
                    toMultiSelectMode()
                }
            }

            MULTI_SELECT_MODE -> {
                // empty
            }

            CONFIRM_MODE_COPY -> {
                // empty
            }

            CONFIRM_MODE_MOVE -> {
                // empty
            }

            CONFIRM_MODE_UNZIP -> {
                // empty
            }

            CONFIRM_MODE_UNZIP_HERE -> {
                // empty
            }
        }
    }

    fun onBackPressed(mode:Int = selectMode.value){
        when (mode) {
            DEFAULT_MODE -> {
                currentDirectory.value?.parentFile?.let {
                    viewModelScope.launch {
                        if (it.isAccessible_td()) {
                            updateDirectory(it)
                        }
                    }
                }
            }
            MULTI_SELECT_MODE ->{
                viewModelScope.launch {
                    modeManager.toDefaultSelectMode()
                }
            }

            else -> {
                currentDirectory.value?.parentFile?.let {
                    viewModelScope.launch {
                        if (it.isAccessible_td()) {
                            updateDirectory(it)
                        }
                    }
                }
            }
        }
    }

    fun refreshFileList(directory: File? = currentDirectory.value){
        viewModelScope.launch {
            directory?.let {
                updateDirectory(it)
            }
        }
    }

    private suspend fun updateDirectory(file:File){
        model.setCurrentDirectory(file)
        val listFiles = file.listFiles()

        notAccessible.value = listFiles == null
        listFiles?.let {
            isEmpty.value = it.isEmpty()
        }
    }

    //------------------------------

    fun onFileClick(file: File){

    }

    fun confirmMove(files:Array<File>,target: File){

    }

    fun confirmCopy(files: Array<File>,target:File){

    }

    fun confirmDelete(files:Array<File>){

    }



}