package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.FileListUiRepository
import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.openFileFromUri_td
import com.todokanai.filemanager.tools.independent.withPrevious_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    private val uiRepo: FileListUiRepository,
    val module: FileModule
) : ViewModel(), FileListViewModelLogics {

    private val _uiState = MutableStateFlow(FileListUiState())
    val uiState = _uiState.asStateFlow()

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
        viewModelScope.launch {
            module.currentDirectory.withPrevious_td().collect {
                _uiState.update { currentState ->
                    currentState.copy(lastKnownDirectory = it)
                }
            }
        }
    }

    override val fileHolderList
        get() = uiRepo.listFiles.map { list ->
            list.map {
                FileHolderItem.fromFile(it)
            }
        }

    override val dirTree
        get() = uiRepo.dirTree.map { tree ->
            tree.map {
                DirectoryHolderItem.fromFile(it)
            }
        }

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        setCurrentDirectory(item.absolutePath)
    }

    override fun onFileClick(context: Context, item: FileHolderItem) {
        viewModelScope.launch {
            if (item.isDirectory) {
                setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a file: ${item.name}")
                openFileFromUri_td(
                    context = context,
                    uri = File(item.absolutePath).toUri(),
                    mimeType = getMimeType_td(item.absolutePath)
                )
            }
        }
    }

    override fun setCurrentDirectory(directory: String) {
        viewModelScope.launch {
            module.setCurrentDirectory(directory)
        }
    }

    fun onBackPressed() {
        val parent = File(module.currentDirectory.value).parentFile
        parent?.let {
            setCurrentDirectory(it.absolutePath)
        }
    }

    /** 새 경로로 이동할 때, scroll 할 위치 가져오기 ( auto scroll 이 필요하다면 ) **/
    fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int {
        return listFiles.map { it.absolutePath }.indexOf(lastKnownDirectory)
    }

    fun popupMenuList(selected: Set<FileHolderItem>): List<Pair<String, () -> Unit>> {
        val result = mutableListOf<Pair<String, () -> Unit>>()
        result.add(
            Pair(
                "Upload",
                {
                    println("${selected.map { it.name }}")
                }
            )
        )
        return result
    }
}

data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false,
    val lastKnownDirectory: String? = null
)