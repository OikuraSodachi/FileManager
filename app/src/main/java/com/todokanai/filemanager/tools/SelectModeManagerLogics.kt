package com.todokanai.filemanager.tools

import androidx.annotation.CallSuper
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class SelectModeManagerLogics {
    private val _selectMode = MutableStateFlow<Int>(DEFAULT_MODE)
    val selectMode = _selectMode.asStateFlow()

    @CallSuper
    open fun toDefaultMode(){
        _selectMode.value = DEFAULT_MODE
    }

    @CallSuper
    open fun toMultiSelectMode(){
        _selectMode.value = MULTI_SELECT_MODE
    }

    @CallSuper
    open fun toConfirmCopyMode(){
        _selectMode.value = CONFIRM_MODE_COPY
    }

    @CallSuper
    open fun toConfirmMoveMode(){
        _selectMode.value = CONFIRM_MODE_MOVE
    }

}