package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    private val dsRepo: DataStoreRepository,
    val module: FileModule
) : FileListViewModelLogics() {

    private val _uiState = MutableStateFlow(FileListUiState())
    val uiState: StateFlow<FileListUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            fileHolderList.collect {
                _uiState.update { currentState ->
                    currentState.copy(listFiles = it, emptyDirectoryText = it.isEmpty())
                }
            }
        }
        viewModelScope.launch {
            dirTree.collect {
                _uiState.update { currentState ->
                    currentState.copy(dirTree = it)
                }
            }
        }
        viewModelScope.launch {
            module.notAccessible.collect {
                _uiState.update { currentState ->
                    currentState.copy(accessFailText = it)
                }
            }
        }
    }

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

    override fun onDirectoryClick(file: File) {
        module.updateCurrentPath(file.absolutePath)
    }

    override fun onFileClick(context: Context, item: File) {
        module.onFileClick(context, item)
    }

    fun onBackPressed() = module.onBackPressedCallback()

}

data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<File> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false
)