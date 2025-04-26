package com.todokanai.filemanager.myobjects

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class Variables {

    companion object {
        /** Todo: 얘 여기에 배치하는게 맞는지? **/
        val storages = MutableStateFlow<Array<File>>(emptyArray())
    }
}