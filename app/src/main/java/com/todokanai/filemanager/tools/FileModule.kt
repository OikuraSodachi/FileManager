package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import com.todokanai.filemanager.tools.independent.dirTree_td
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(val coroutineDispatcher: CoroutineDispatcher) :
    FileModuleLogics<File>() {

    val dirTree = currentDirectory.map {
        dirTree_td(File(it)).map {
            it.absolutePath
        }
    }

    /** whether currentPath is Accessible **/
    val notAccessible = currentDirectory.map { !isDirectoryValid(it) }

    val listFiles = currentDirectory.map { directory ->
        File(directory).listFiles() ?: emptyArray()
    }

    override suspend fun isDirectoryValid(directory: String): Boolean =
        withContext(coroutineDispatcher) {
            return@withContext File(directory).listFiles() != null
        }
}