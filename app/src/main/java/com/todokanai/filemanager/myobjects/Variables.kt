package com.todokanai.filemanager.myobjects

import android.os.Environment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class Variables {

    companion object{

        val defaultStorage = Environment.getExternalStorageDirectory()

        val storages = MutableStateFlow<Array<File>>(emptyArray())

        val initialStorage  = File("/storage/emulated/0")
    }
}