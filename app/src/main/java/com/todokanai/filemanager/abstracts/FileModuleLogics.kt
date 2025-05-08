package com.todokanai.filemanager.abstracts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** @param Type type of file (File, FTPFile, etc...)
 * @param initialPath initial absolutePath **/
abstract class FileModuleLogics<Type : Any>(initialPath: String) {

    private val _currentDirectory = MutableStateFlow<String>(initialPath)
    val currentDirectory = _currentDirectory.asStateFlow()

    suspend fun setCurrentDirectory(directory: String) {
        if (isDirectoryValid(directory)) {
            _currentDirectory.value = directory
        }
    }

    /** @param directory current directory
     * @return files in directory **/
    abstract suspend fun getListFiles(directory: String): Array<Type>

    /** @param directory absolutePath of directory to open
     * @return whether directory is accessible **/
    abstract suspend fun isDirectoryValid(directory: String): Boolean
}