package com.todokanai.filemanager.tools

import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class SelectModeManager {

    private val _selectMode = MutableStateFlow<Int>(Constants.DEFAULT_MODE)
    val selectMode : StateFlow<Int>
        get() = _selectMode

    private val _selectedFiles = MutableStateFlow<Array<File>>(emptyArray())
    val selectedFiles : StateFlow<Array<File>>
        get() = _selectedFiles

    fun setSelectedFiles(file:File){
        val list = selectedFiles.value.toMutableList()
        if(list.contains(file)){
            _selectedFiles.value = list.minus(file).toTypedArray()
        } else{
            _selectedFiles.value = list.plus(file).toTypedArray()
        }
    }

    fun clearSelectedFiles(){
        _selectedFiles.value = emptyArray()
    }

    suspend fun selectAll(){
   //     _selectedFiles.value = fileModel.listFiles.first() ?: emptyArray()
    }

    //---------------------

    fun changeSelectMode(mode:Int){
        _selectMode.value = mode
        if(mode == Constants.DEFAULT_MODE){
            clearSelectedFiles()
        }
    }
}