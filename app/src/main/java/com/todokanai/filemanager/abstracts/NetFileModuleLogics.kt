package com.todokanai.filemanager.abstracts

import androidx.annotation.CallSuper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.apache.commons.net.ftp.FTPFile

abstract class NetFileModuleLogics(defaultPath: String) {
    private val _currentDirectory = MutableStateFlow<String>(defaultPath)
    val currentDirectory: StateFlow<String>
        get() = _currentDirectory

    val currentFTPFile = MutableStateFlow<FTPFile>(FTPFile())

    abstract val itemList: Flow<Array<FTPFile>>

    @CallSuper
    open suspend fun setCurrentDirectory(absolutePath: String) {
        val listFiles = requestListFilesFromNet(absolutePath)
        if(listFiles!=null){
            _currentDirectory.value = absolutePath
        }
//        //-----
//        if (isFileValid(absolutePath)) {
//            _currentDirectory.value = absolutePath
//        }
    }

    /** returns true if the file is valid **/
    abstract fun isFileValid(absolutePath: String): Boolean

    /** [directory] 내부의 파일 목록 가져오기
     * @return null on failure **/
    abstract suspend fun requestListFilesFromNet(directory: String): Array<FTPFile>?

   // abstract fun toParentDirectory(current: String = currentDirectory.value)
}