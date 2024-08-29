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
import com.todokanai.filemanager.myobjects.Objects.fileModule
import com.todokanai.filemanager.repository.DataStoreRepository
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

    private val module = fileModule
    private val modeManager = Objects.modeManager
    private val currentDirectory = module.currentPath

    val notAccessible =  module.notAccessible
    val isEmpty = module.isEmpty        // is directory.listFiles() empty

    val selectedFiles = modeManager.selectedFiles

    private val sortMode = dsRepo.sortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = BY_DEFAULT
    )

    val directoryList = module.dirTree

    val fileHolderListNew = module.files.map { listFiles ->
        sortedFileList_td(listFiles,sortMode.value)
    }


    // ------------------------

    fun onDirectoryClick(directory:File,mode:Int){
        viewModelScope.launch {
            if(mode != MULTI_SELECT_MODE) {
                if (directory.isAccessible_td()) {        // 접근 가능여부 체크
                    updateDirectory(directory)
                }
            }
        }
    }

    fun onClick(context: Context,file: File,mode:Int){
        when (mode) {
            DEFAULT_MODE -> {
                viewModelScope.launch {
                    if (file.isDirectory) {
                        updateDirectory(file)
                    } else {
                        module.onFileClick(context,file)
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

    fun onLongClick(file: File,mode:Int,){
        when (mode) {
            DEFAULT_MODE -> {
                modeManager.run{
                    setSelectedFiles(file)
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

    fun onBackPressed(mode:Int){
        when (mode) {
            DEFAULT_MODE -> {
                currentDirectory.value.parentFile?.let {
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
                currentDirectory.value.parentFile?.let {
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

    private fun updateDirectory(file:File) = module.updateCurrentPath(file)


    //------------------------------
    fun onConfirmTest(selectMode:Int){
        when(selectMode){
            CONFIRM_MODE_COPY ->{

            }
            CONFIRM_MODE_MOVE ->{

            }
            CONFIRM_MODE_UNZIP ->{

            }
            CONFIRM_MODE_UNZIP_HERE ->{

            }
        }
    }



}