package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Objects.fileModule
import com.todokanai.filemanager.myobjects.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor() : ViewModel(){

    val module = fileModule
    val storageHolderList by lazy{
        Variables.storages.map{
            it.map{ file ->
                file
            }
        }
    }

    fun onItemClick(file:File){
        viewModelScope.launch {
            module.updateCurrentPath(file)
        }
    }
}