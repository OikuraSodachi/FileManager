package com.todokanai.filemanager.viewmodel

import android.content.Context
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    private val dsRepo: DataStoreRepository,
    val module: FileModule
) : FileListViewModelLogics() {

    override val fileHolderList
        get() = combine(
            module.listFiles,
            dsRepo.sortBy
        ) { listFiles, mode ->
            sortedFileList_td(listFiles.map { File(it) }.toTypedArray(), mode).map {
                FileHolderItem.fromFile(it)
            }
        }.flowOn(Dispatchers.IO)

    override val dirTree
        get() = module.dirTree.map {
            it.map {
                DirectoryHolderItem.fromFile(File(it))
            }
        }

    override val notAccessible: Flow<Boolean>
        get() = module.notAccessible

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        module.updateCurrentPath(item.absolutePath)
    }

    override fun onFileClick(context: Context, item: File) {
        module.onFileClick(item.absolutePath)
    }

    fun onBackPressed() = module.onBackPressedCallback()
}