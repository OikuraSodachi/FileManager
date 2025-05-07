package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.tools.FileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(val module: FileModule) : ViewModel() {

    val storageHolderList = Variables.storages.map { it.toList() }

    fun onItemClick(file: File) {
        viewModelScope.launch {
            module.setCurrentDirectory(file.absolutePath)
        }
    }
}