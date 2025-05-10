package com.todokanai.filemanager.repository

import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class FileListUiRepository @Inject constructor(
    val module: FileModule,
    dsRepo: DataStoreRepository
) {
    val currentDirectory = module.currentDirectory
    val notAccessible = module.notAccessible

    /** 경로 내 File 목록 **/
    val listFiles = combine(
        currentDirectory,
        dsRepo.sortBy
    ) { directory, mode ->
        sortedFileList_td(module.getListFiles(directory), mode)
    }

    /** directory 가 비어있는지 여부 **/
    val emptyDirectoryText = listFiles.map {
        it.isEmpty()
    }

    val dirTree = module.dirTree.map { tree ->
        tree.map {
            File(it)
        }
    }
}