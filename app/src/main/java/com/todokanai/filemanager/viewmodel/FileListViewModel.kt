package com.todokanai.filemanager.viewmodel

import android.content.Context
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository,val module:FileModule): FileListViewModelLogics(){

    private val _uiState = MutableStateFlow(FileListUiState())
    val uiState: StateFlow<FileListUiState>
        get() = _uiState

    override val fileHolderList
        get() = combine(
            module.listFiles,
            dsRepo.sortBy
        ){
         listFiles,mode ->
            sortedFileList_td(listFiles,mode)
        }

    override val dirTree
        get() = module.currentPath.map{ File(it).dirTree() }

    override suspend fun updateUI() {
        fileHolderList.collect{
            _uiState.update { currentState ->
                currentState.copy(listFiles = it, emptyDirectoryText = it.isEmpty())
            }
        }
        dirTree.collect{
            _uiState.update { currentState ->
                currentState.copy(dirTree = it)
            }
        }
        module.notAccessible.collect{
            _uiState.update { currentState ->
                currentState.copy(accessFailText = it)
            }
        }
    }

    override fun onDirectoryClick(file:File){
        module.updateCurrentPath(file.absolutePath)
    }

    override fun onFileClick(context: Context, item:File) {
        module.onFileClick(context, item)
    }

    fun onBackPressed() = module.onBackPressedCallback()

    /** Todokanai
     *
     *  == File.dirTree_td() **/
    private fun File.dirTree(): List<File> {
        val result = mutableListOf<File>()
        var now = this
        while (now.parentFile != null) {
            result.add(now)
            now = now.parentFile!!
        }
        return result.reversed()
    }

}

data class FileListUiState(
    val listFiles:List<File> = emptyList(),
    val dirTree:List<File> = emptyList(),
    val emptyDirectoryText:Boolean = false,
    val accessFailText:Boolean = false
)