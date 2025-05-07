package com.todokanai.filemanager.repository

import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class FileListUiRepository @Inject constructor(
    val module: FileModule,
    val dsRepo: DataStoreRepository
) {

    /** 경로 내 File 목록 **/
    val listFiles = combine(
        module.listFiles,
        dsRepo.sortBy
    ) { listFiles, mode ->
        sortedFileList_td(listFiles.map { File(it) }.toTypedArray(), mode)
    }

    val dirTree = module.dirTree.map { tree ->
        tree.map {
            File(it)
        }
    }
}