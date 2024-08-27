package com.todokanai.filemanager.viewmodel.submodel

import android.content.Context
import com.todokanai.filemanager.myobjects.Variables.Companion.initialStorage
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.openFile_td
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

class FileListModel() {

    val notAccessible = MutableStateFlow(false)
    val isEmpty = MutableStateFlow(false)        // is directory.listFiles() empty

    private val _currentDirectory = MutableStateFlow<File?>(initialStorage)
    val currentDirectory : StateFlow<File?>
        get() = _currentDirectory

    fun setCurrentDirectory(directory:File?) {
        _currentDirectory.value = directory
    }

    val listFiles = _currentDirectory.map {
        it?.listFiles()
    }


    fun openFile(context: Context,file: File) {
        val mimeType = getMimeType_td(file.name)
        openFile_td(context,file,mimeType)
    }


    fun copyFiles(files:Array<File>,targetDirectory:File){
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    fun moveFiles(files:Array<File>,targetDirectory:File){
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    fun deleteFiles(files: Array<File>){
        println("delete: ${files.map{it.name}}")
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    fun unzipFile(files: Array<File>, targetDirectory: File){

        CoroutineScope(Dispatchers.IO).launch {

        }
    }
}