package com.todokanai.filemanager.tools

import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

/** RecyclerAdapter쪽에 통합해야 할듯?**/
class SelectModeManager {

    private val _selectMode = MutableStateFlow<Int>(Constants.DEFAULT_MODE)
    val selectMode : StateFlow<Int>
        get() = _selectMode

    fun selectMode():Int{
        return selectMode.value
    }

    fun isMultiSelectMode():Boolean{
        return selectMode.value == MULTI_SELECT_MODE
    }

    fun isNotMultiSelectMode():Boolean{
        return selectMode.value != MULTI_SELECT_MODE
    }

    fun isDefaultMode():Boolean{
        return selectMode.value == DEFAULT_MODE
    }

    //---------------------

    fun changeSelectMode(mode:Int){
        _selectMode.value = mode
    }

    //--------------
    val isMultiSelectMode  = selectMode.map { mode ->
        mode == MULTI_SELECT_MODE
    }

    val isDefaultMode = selectMode.map { mode ->
        mode == DEFAULT_MODE
    }

    //-----------------

    fun onDefaultMode_new() = changeSelectMode(DEFAULT_MODE)
    fun onConfirmMoveMode_new() = changeSelectMode(CONFIRM_MODE_MOVE)
    fun onConfirmCopyMode_new() = changeSelectMode(CONFIRM_MODE_COPY)
}