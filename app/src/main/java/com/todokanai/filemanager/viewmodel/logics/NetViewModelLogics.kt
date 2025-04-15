package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile

abstract class NetViewModelLogics : ViewModel() {
    private val _uiState = MutableStateFlow(NetUiState())
    val uiState: StateFlow<NetUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            dirTree.collect{
                _uiState.update { currentState ->
                    currentState.copy(
                        currentPath = it
                    )
                }
            }
        }
        viewModelScope.launch {
            itemList.collect{
                _uiState.update { currentState ->
                    currentState.copy(
                        itemList = it
                    )
                }
            }
        }
        viewModelScope.launch {
            isLoggedIn.collect{
                _uiState.update { currentState ->
                    currentState.copy(
                        loggedIn = it
                    )
                }
            }
        }
    }

    protected abstract val dirTree: Flow<List<DirectoryHolderItem>>
    protected abstract val itemList: Flow<List<FileHolderItem>>
    protected abstract val isLoggedIn:Flow<Boolean>

    abstract fun login()
    abstract fun onItemClick(item: FileHolderItem)
    abstract fun toParent()

    protected fun FTPFile.toFileHolderItem(currentDirectory:String): FileHolderItem {
        val absolutePathTemp = "${currentDirectory}/${this.name}"
        return FileHolderItem(
            absolutePath = absolutePathTemp,
            name = this.name,
            size = readableFileSize_td(this.size),
            lastModified = this.timestamp.timeInMillis,
            isDirectory = this.isDirectory
        )
    }
}

data class NetUiState(
    val currentPath:List<DirectoryHolderItem> = emptyList(),
    val itemList: List<FileHolderItem> = emptyList(),
    val loggedIn: Boolean = false
)