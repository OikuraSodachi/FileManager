package com.todokanai.filemanager.tools.independent

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(defaultPath:File) {

    /** 현재 보고있는 Directory
     *
     *  Primary Key(?)
     * **/
    private val _currentPath = MutableStateFlow<File>(defaultPath)
    val currentPath : StateFlow<File>
        get() = _currentPath

    private val listFiles = currentPath.map {
        it.listFiles()
    }

    /** directory tree **/
    val dirTree = currentPath.map { file ->
        file.dirTree()
    }

    /** array of files to show **/
    val files = listFiles.map{
        it ?: emptyArray()
    }

    /** whether currentPath is Accessible **/
    val notAccessible = listFiles.map {
        it == null
    }

    /** whether currentPath is a empty directory **/
    val isEmpty = files.map{
        it.isEmpty()
    }

    /** setter for currentPath **/
    fun updateCurrentPath(file: File){
        _currentPath.value = file
    }

    fun onFileClick(context: Context, file: File){
        val mimeType = getMimeType_td(file.name)
        openFile_td(context,file,mimeType)
    }


    /** Todokanai
     *
     *  == File.dirTree_td()
     * */
    private fun File.dirTree(): List<File> {
        val result = mutableListOf<File>()
        var now = this
        while (now.parentFile != null) {
            result.add(now)
            now = now.parentFile!!
        }
        return result.reversed()
    }
}