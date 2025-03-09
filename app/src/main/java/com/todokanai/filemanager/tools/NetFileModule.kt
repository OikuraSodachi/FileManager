package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.BaseNetFileModule
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
    override val itemList: StateFlow<List<File>>
        get() = TODO("Not yet implemented")


}