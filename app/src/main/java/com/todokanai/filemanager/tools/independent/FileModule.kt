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

    /** directory tree **/
    val dirTree = currentPath.map { file ->
        file.dirTree()
    }

    /** array of files to show **/
    val listFiles = currentPath.map {
        it.listFiles() ?: emptyArray()
    }

    /** whether currentPath is Accessible **/
    val notAccessible = currentPath.map {
        it.listFiles() == null
    }
    /** currentPath 값에 변화가 없어서 listFiles 갱신이 되지 않고 있음 **/
    fun refreshListFiles(file: File = currentPath.value) = updateCurrentPath(file)


    /** setter for currentPath **/
    fun updateCurrentPath(directory: File) {
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
            updateCurrentPath(file)
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