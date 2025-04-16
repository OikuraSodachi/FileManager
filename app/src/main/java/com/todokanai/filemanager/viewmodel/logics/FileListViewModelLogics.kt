package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

abstract class FileListViewModelLogics : ViewModel() {
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

    abstract val dirTree: Flow<List<DirectoryHolderItem>>

    abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract val notAccessible: Flow<Boolean>

    abstract fun onDirectoryClick(item: DirectoryHolderItem)

    abstract fun onFileClick(context: Context, item: File)

}

data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false
)