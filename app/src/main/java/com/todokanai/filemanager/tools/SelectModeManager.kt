package com.todokanai.filemanager.tools

import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class SelectModeManager {
    /*
    temporary dummyData
    companion object{
        val DEFAULT_MODE : Int = 10
        val MULTI_SELECT_MODE : Int = 11
        val CONFIRM_MODE_COPY : Int = 12
        val CONFIRM_MODE_MOVE : Int = 13
        val CONFIRM_MODE_UNZIP : Int = 14
        val CONFIRM_MODE_UNZIP_HERE : Int = 15
    }
     */


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
}