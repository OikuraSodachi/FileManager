package com.todokanai.filemanager.repository

import com.todokanai.filemanager.tools.FileModule
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
        module.listFiles,
        dsRepo.sortBy
    ) { listFiles, mode ->
        listFileSorter(listFiles,mode)
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

    /** Todo: listFiles 정렬 로직 만들기 **/
    private fun listFileSorter(listFiles: Array<File>, sortMode: String?):List<File>{
        return when (sortMode) {
            "" -> listFiles.sortedBy{it.name}
            else -> listFiles.sortedBy{it.name}
        }
    }
}