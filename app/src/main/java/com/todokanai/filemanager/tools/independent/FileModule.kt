package com.todokanai.filemanager.tools.independent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(defaultPath: String) {

    /** 현재 보고있는 Directory
     *
     *  Primary Key(?) **/
    private val _currentPath = MutableStateFlow<String>(defaultPath)
    val currentPath: StateFlow<String>
        get() = _currentPath

    /** array of files to show **/
    val listFiles = currentPath.map {
        File(it).listFiles()?.map {
            it.absolutePath
        }?.toTypedArray() ?: emptyArray()
    }

    val dirTree = currentPath.map {
        File(it).dirTree().map {
            it.absolutePath
        }
    }

    /** whether currentPath is Accessible **/
    val notAccessible = currentPath.map { File(it).listFiles() == null }

    /** setter for currentPath **/
    fun updateCurrentPath(directory: String) {
        if (isAccessible_td(File(directory))) {        // 접근 가능여부 체크
            _currentPath.value = directory
        }
    }

//    /** file.isDirectory == true일 경우, currentPath 값을 update
//     *
//     *  false일 경우, Intent.ACTION_VIEW (파일 열기) 실행 **/
//    override fun onFileClick(file: File,openFileFromUri:(uri: Uri,mimeType:String)->Unit) {
//        if (file.isDirectory) {
//            updateCurrentPath(file.absolutePath)
//        } else {
//            val mimeType = getMimeType_td(file.name)
//            openFileFromUri(file.toUri(),mimeType)
//
//            openFileFromUri_td(
//                appContext,
//                file.toUri(),
//                mimeType
//            ) // Todo: appContext 써도 무방한지?
//        }
//    }

//    fun onBackPressedCallback() {
//        File(currentPath.value).parentFile?.let {
//            updateCurrentPath(it.absolutePath)
//        }
//    }

    /** Todokanai
     *
     *  == File.dirTree_td() **/
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