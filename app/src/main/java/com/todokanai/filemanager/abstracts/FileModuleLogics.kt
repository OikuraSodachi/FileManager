package com.todokanai.filemanager.abstracts

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class FileModuleLogics {

    private val _currentDirectory = MutableSharedFlow<String>(1)
    val currentDirectory = _currentDirectory.asSharedFlow()

    suspend fun setCurrentDirectory(directory: String) {
        if (isDirectoryValid(directory)) {
            _currentDirectory.emit(directory)
        }
    }

    /** @param directory absolutePath of directory to open
     * @return whether directory is accessible **/
    abstract suspend fun isDirectoryValid(directory: String): Boolean
}