package com.todokanai.filemanager.myobjects

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class Variables {

    companion object {
        val storages = MutableStateFlow<Array<File>>(emptyArray())
    }
}