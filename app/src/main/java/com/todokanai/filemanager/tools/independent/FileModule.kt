package com.todokanai.filemanager.tools.independent

import android.content.Context
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(defaultPath:File) {

    /** 현재 보고있는 Directory
     *
     *  Primary Key(?)
     * **/
    /*
    private val _currentPath = MutableStateFlow<File>(defaultPath)
    val currentPath : StateFlow<File>
        get() = _currentPath
     */

    private val _currentPath = MutableStateFlow<String>(defaultPath.absolutePath)
    val currentPath : StateFlow<String>
        get() = _currentPath

    /** listFiles 값 refresh 용도 **/
    private val updaterFlow = MutableStateFlow<Boolean>(false)

    /** directory tree **/
    val dirTree = currentPath.map { File(it).dirTree() }

    /** array of files to show
     *
     * TODO : combine 대신 sharedFlow를 이용해서  refresh 기능을 만들 것
     * **/
    val listFiles = combine(
        currentPath,
        updaterFlow
    ){
        path,updater ->
        File(path).listFiles() ?: emptyArray()
    }

    /** whether currentPath is Accessible **/
    val notAccessible = currentPath.map { File(it).listFiles() == null }

    fun refreshListFiles(){ updaterFlow.value =!updaterFlow.value }

    /** setter for currentPath **/
    fun updateCurrentPath(directory: String) {
        if (File(directory).isAccessible_td()) {        // 접근 가능여부 체크
            _currentPath.value = directory
        }
    }

    /** file.isDirectory == true일 경우, currentPath 값을 update
     *
     *  false일 경우, Intent.ACTION_VIEW (파일 열기) 실행 **/
    /*
    fun onFileClick(context: Context, file: File){
        if(file.isDirectory){
            updateCurrentPath(file)
        } else {
            val mimeType = getMimeType_td(file.name)
            openFile_td(context, file, mimeType)
        }
    }
     */

    fun onFileClick(context: Context, item:FileHolderItem){
        if(item.isDirectory()){
            updateCurrentPath(item.absolutePath)
        } else {
            val mimeType = getMimeType_td(item.absolutePath)
            openFileFromUri_td(context, item.uri, mimeType)
        }
    }

    fun onBackPressedCallback(){
        File(currentPath.value).parentFile?.let{
            updateCurrentPath(it.absolutePath)
        }
    }

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