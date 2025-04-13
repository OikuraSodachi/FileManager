package com.todokanai.filemanager.viewmodel

import android.content.Context
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.viewmodel.logics.FileListUiState
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
            sortedFileList_td(listFiles, mode).map {
                it.toFileHolderItem()
            }
        }

    override val dirTree
        get() = module.currentPath.map { File(it).dirTree() }

    override suspend fun updateUI(uiState: MutableStateFlow<FileListUiState>) {
        fileHolderList.collect {
            uiState.update { currentState ->
                currentState.copy(listFiles = it, emptyDirectoryText = it.isEmpty())
            }
        }
        dirTree.collect {
            uiState.update { currentState ->
                currentState.copy(dirTree = it)
            }
        }
        module.notAccessible.collect {
            uiState.update { currentState ->
                currentState.copy(accessFailText = it)
            }
        }
    }

    override fun onDirectoryClick(file: File) {
        module.updateCurrentPath(file.absolutePath)
    }

    override fun onFileClick(context: Context, item: File) {
        module.onFileClick(context, item)
    }

    fun onBackPressed() = module.onBackPressedCallback()

}
