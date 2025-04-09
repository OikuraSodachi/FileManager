package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.tools.independent.uploadFileToFtp_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(dsRepo:DataStoreRepository,val module:FileModule): FileListViewModelLogics(){

    private val _uiState = MutableStateFlow(FileListUiState())
    val uiState: StateFlow<FileListUiState>
        get() = _uiState

    private val fileHolderList = combine(
        module.listFiles,
        dsRepo.sortBy
    ){
        listFiles,mode ->
        sortedFileList_td(listFiles,mode).map{
            it.toFileHolderItem()
        }
    }

    override suspend fun updateUI() {
        fileHolderList.collect{
            _uiState.update { currentState ->
                currentState.copy(listFiles = it, emptyDirectoryText = it.isEmpty())
            }
        }
        module.dirTree.collect{
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

    override fun onFileClick(context: Context, item:FileHolderItem) {
        module.onFileClick(context, item)
    }

    fun onBackPressed() = module.onBackPressedCallback()

    fun uploadToNas(selected: File){
        CoroutineScope(Dispatchers.IO).launch{
            uploadFileToFtp_td(
                server = "",
                username = "",
                password = "",
                localFilePath = selected.absolutePath,
                remoteFilePath = ""
            )
        }
    }

    private fun File.toFileHolderItem():FileHolderItem{
        val sizeText: String =
            if(this.isDirectory){
                "${this.listFiles()?.size} 개"
            }else{
                readableFileSize_td(this.length())
            }

        return FileHolderItem(
            absolutePath = this.absolutePath,
            size = sizeText,
            lastModified = this.lastModified(),
            uri = this.toUri()
        )
    }

}

data class FileListUiState(
    val listFiles:List<FileHolderItem> = emptyList(),
    val dirTree:List<File> = emptyList(),
    val emptyDirectoryText:Boolean = false,
    val accessFailText:Boolean = false
)