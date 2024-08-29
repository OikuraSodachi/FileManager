package com.todokanai.filemanager.tools

import com.todokanai.filemanager.myobjects.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class SelectModeManager {

    private val _selectMode = MutableStateFlow<Int>(Constants.DEFAULT_MODE)
    val selectMode : StateFlow<Int>
        get() = _selectMode


    /** View 단계로 옮겨놔야 함 **/
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

    fun toDefaultSelectMode(){
        _selectMode.value = Constants.DEFAULT_MODE
        clearSelectedFiles()
    }

    fun toMultiSelectMode(){
        _selectMode.value = Constants.MULTI_SELECT_MODE
    }

    fun toConfirmCopyMode(){
        _selectMode.value = Constants.CONFIRM_MODE_COPY
    }

    fun toConfirmMoveMode(){
        _selectMode.value = Constants.CONFIRM_MODE_MOVE
    }

    fun toConfirmUnzipMode(){
        _selectMode.value = Constants.CONFIRM_MODE_UNZIP
    }

    fun toConfirmUnzipHereMode(){
        _selectMode.value = Constants.CONFIRM_MODE_UNZIP_HERE
    }
    //---------------------

    fun changeSelectMode(mode:Int){
        _selectMode.value = mode
    }
}