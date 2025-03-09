package com.todokanai.filemanager.abstracts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.apache.commons.net.ftp.FTPFile

abstract class BaseNetFileModule(defaultPath:String) {
    val currentDirectory = MutableStateFlow<String>(defaultPath)
    val itemList: Flow<Array<FTPFile>>
        get() = currentDirectory.map {
            requestListFilesFromNet(it)
        }.flowOn(
            Dispatchers.Default
        )

    /** [directory] 내부의 파일 목록 가져오기 **/
    abstract suspend fun requestListFilesFromNet(directory:String):Array<FTPFile>
}