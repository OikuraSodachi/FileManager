package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import com.todokanai.filemanager.tools.independent.dirTree_td
import kotlinx.coroutines.flow.map
import java.io.File

/** 파일탐색기 기능을 위한 class **/
class FileModule(defaultPath: String) : FileModuleLogics<File>(defaultPath) {

    val dirTree = currentDirectory.map {
        dirTree_td(File(it)).map {
            it.absolutePath
        }
    }

    /** whether currentPath is Accessible **/
    val notAccessible = currentDirectory.map { !isDirectoryValid(it) }

    val listFiles = currentDirectory.map{
        getContentFiles(it)
    }

    private suspend fun getContentFiles(directory: String): Array<File> {
        return File(directory).listFiles() ?: emptyArray()
    }

    override suspend fun isDirectoryValid(directory: String): Boolean {
        return File(directory).listFiles() != null
    }

//    /** file.isDirectory == true일 경우, currentPath 값을 update
//     *
//     *  false일 경우, Intent.ACTION_VIEW (파일 열기) 실행 **/
//    override fun onFileClick(file: File,openFileFromUri:(uri: Uri,mimeType:String)->Unit) {
//        if (file.isDirectory) {
//            updateCurrentPath(file.absolutePath)
//        } else {
//            val mimeType = getMimeType_td(file.name)
//            openFileFromUri(file.toUri(),mimeType)
//
//            openFileFromUri_td(
//                appContext,
//                file.toUri(),
//                mimeType
//            ) // Todo: appContext 써도 무방한지?
//        }
//    }
}