package com.todokanai.filemanager.interfaces

import kotlinx.coroutines.flow.Flow

interface FileModuleLogics {

    val listFiles: Flow<Array<String>>

    fun onFileClick(path: String)

    val dirTree: Flow<List<String>>

}