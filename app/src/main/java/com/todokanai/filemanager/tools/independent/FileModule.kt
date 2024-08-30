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
    fun updateCurrentPathSafe(directory: File) {
        if (directory.isAccessible_td()) {        // 접근 가능여부 체크
            _currentPath.value = directory
        }
    }

    /** file.isDirectory == true일 경우, currentPath 값을 update
     *
     *  false일 경우, Intent.ACTION_VIEW (파일 열기) 실행
     * **/
    fun onFileClick(context: Context, file: File){
        if(file.isDirectory){
            updateCurrentPathSafe(file)
        } else {
            val mimeType = getMimeType_td(file.name)
            openFile_td(context, file, mimeType)
        }
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