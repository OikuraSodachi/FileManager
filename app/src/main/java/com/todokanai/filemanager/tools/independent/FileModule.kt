package com.todokanai.filemanager.tools.independent

import android.content.Context
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(defaultPath:File) {

    /** 현재 보고있는 Directory
     *
     *  Primary Key(?) **/
    private val _currentPath = MutableStateFlow<String>(defaultPath.absolutePath)
    val currentPath : StateFlow<String>
        get() = _currentPath

    /** array of files to show **/
    val listFiles = currentPath.map{ File(it).listFiles() ?: emptyArray() }

    /** whether currentPath is Accessible **/
    val notAccessible = currentPath.map { File(it).listFiles() == null }

    /** setter for currentPath **/
    fun updateCurrentPath(directory: String) {
        if (isAccessible_td(File(directory))) {        // 접근 가능여부 체크
            _currentPath.value = directory
        }
    }

    /** file.isDirectory == true일 경우, currentPath 값을 update
     *
     *  false일 경우, Intent.ACTION_VIEW (파일 열기) 실행 **/
    fun onFileClick(context: Context, file:File){
        if(file.isDirectory){
            updateCurrentPath(file.absolutePath)
        } else {
            val mimeType = getMimeType_td(file.name)
            openFileFromUri_td(context, file.toUri(), mimeType)
        }
    }

    fun onBackPressedCallback(){
        File(currentPath.value).parentFile?.let{
            updateCurrentPath(it.absolutePath)
        }
    }
}