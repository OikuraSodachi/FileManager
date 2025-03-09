package com.todokanai.filemanager.abstracts

import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

/** @param serverIp ip of the ftp server
 * @param userId id
 * @param userPassword password
 * @param defaultDirectory initial directory **/
abstract class BaseNetFileModule(
    val serverIp:String,
    val userId:String,
    val userPassword:String,
    val defaultDirectory:String,
    val port:Int = 21
) {
    val currentDirectory = MutableStateFlow<File>(File(defaultDirectory))
    abstract val itemList: StateFlow<List<FileHolderItem>>

    /** [directory] 내부의 파일 목록 가져오기 **/
    abstract suspend fun requestListFilesFromNet(directory:File):Array<File>

}