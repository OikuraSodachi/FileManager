package com.todokanai.filemanager.tools

import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

class SelectModeManager {

    private val _selectMode = MutableStateFlow<Int>(Constants.DEFAULT_MODE)
    val selectMode : StateFlow<Int>
        get() = _selectMode

    private val _selectedFiles = MutableStateFlow<Array<File>>(emptyArray())
    val selectedFiles : StateFlow<Array<File>>
        get() = _selectedFiles

    fun toggleToSelectedFiles(file:File){
        val list = selectedFiles.value.toMutableList()
        if(list.contains(file)){
            _selectedFiles.value = list.minus(file).toTypedArray()
        } else{
            _selectedFiles.value = list.plus(file).toTypedArray()
        }
    }

    fun isMultiSelectMode():Boolean{
        return selectMode.value == MULTI_SELECT_MODE
    }

    fun isDefaultMode():Boolean{
        return selectMode.value == DEFAULT_MODE
    }

    fun selectAll(){
   //     _selectedFiles.value = fileModel.listFiles.first() ?: emptyArray()
    }

    //---------------------

    fun changeSelectMode(mode:Int){
        _selectMode.value = mode
        if(mode == DEFAULT_MODE){
            _selectedFiles.value = emptyArray()
        }
    }

    //--------------
    val isMultiSelectMode  = selectMode.map { mode ->
        mode == MULTI_SELECT_MODE
    }

    val isDefaultMode = selectMode.map { mode ->
        mode == DEFAULT_MODE
    }
}