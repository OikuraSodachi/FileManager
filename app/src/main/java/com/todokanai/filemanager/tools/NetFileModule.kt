package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.BaseNetFileModule
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class NetFileModule(
    serverIp:String,
    userId:String,
    userPassword:String,
    defaultDirectory:String
):BaseNetFileModule(
    serverIp,
    userId,
    userPassword,
    defaultDirectory
) {
    override val itemList: StateFlow<List<FileHolderItem>>
        get() = TODO("Not yet implemented")

    override suspend fun requestListFilesFromNet(directory: File): Array<File> {
        TODO("Not yet implemented")
    }


}