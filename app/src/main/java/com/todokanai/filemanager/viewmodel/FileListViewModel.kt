package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.openFileFromUri_td
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
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
        ) { listFiles, mode ->
            sortedFileList_td(listFiles.map { File(it) }.toTypedArray(), mode).map {
                FileHolderItem.fromFile(it)
            }
        }.flowOn(Dispatchers.IO)

    override val dirTree
        get() = module.dirTree.map { tree ->
            tree.map {
                DirectoryHolderItem.fromFile(File(it))
            }
        }

    override val notAccessible: Flow<Boolean>
        get() = module.notAccessible

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        setCurrentDirectory(item.absolutePath)
    }

    override fun onFileClick(context: Context, item: FileHolderItem) {
        viewModelScope.launch {
            if(item.isDirectory){
                setCurrentDirectory(item.absolutePath)
            }else{
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
        module.updateCurrentPath(directory)
    }

    fun onBackPressed(){
        val parent = File(module.currentPath.value).parentFile
        parent?.let{
            setCurrentDirectory(it.absolutePath)
        }
    }

    fun popupMenuList(selected:Set<FileHolderItem>):List<Pair<String,()->Unit>>{
        val result = mutableListOf<Pair<String,()->Unit>>()
        result.add(
            Pair(
                "Upload",
                {
                    println("${selected.map{it.name}}")
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
    val accessFailText: Boolean = false
)