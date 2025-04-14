package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.abstracts.NetFileModuleLogics
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    val module: FileModule,
    val netModule: NetFileModuleLogics
) : FileListViewModelLogics() {

    private val _isNetModule = MutableStateFlow<Boolean>(false)
    val isNetModule : StateFlow<Boolean>
        get() = _isNetModule

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
            notAccessible.collect {
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
        ) { listFiles, mode->
            sortedFileList_td(listFiles.map { File(it) }.toTypedArray(), mode).map {
                val sizeText: String =
                    if (it.isDirectory) {
                        "${it.listFiles()?.size} 개"
                    } else {
                        readableFileSize_td(it.length())
                    }
                FileHolderItem(
                    absolutePath = it.absolutePath,
                    size = sizeText,
                    lastModified = it.lastModified(),
                    uri = it.toUri()
                )
            }
        }

    override val dirTree
        // get() = module.currentPath.map { File(it).dirTree().map{it.toDirectoryHolderItem()} }
        get() = module.dirTree.map {
            it.map {
                DirectoryHolderItem(
                    name = File(it).name,
                    absolutePath = it
                )
            }
        }

    override val notAccessible: Flow<Boolean>
        get() = module.notAccessible

    override fun onDirectoryClick(absolutePath: String) {
        module.updateCurrentPath(absolutePath)
    }

    override fun onFileClick(context: Context, item: File) {
        module.onFileClick(item.absolutePath)
    }

    fun onBackPressed() = module.onBackPressedCallback()

}


data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false
)