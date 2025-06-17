package com.todokanai.filemanager.myobjects

import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

/** 기능 구현을 위해 일단 companion object 에 두고 사용하는 변수들. 나중에 적절한 위치로 옮길 것 ( memory leak ) **/
class Variables {

    companion object {
        /** Todo: 얘 여기에 배치하는게 맞는지? **/
        val storages = MutableStateFlow<Array<File>>(emptyArray())

        val fileListMode = MutableStateFlow<Int>(DEFAULT_MODE)
        val selectedFilesTemp = MutableStateFlow<Array<File>>(emptyArray<File>())
    }
}