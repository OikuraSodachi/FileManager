package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.openFileFromUri_td
import com.todokanai.filemanager.tools.independent.withPrevious_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    val module: FileModule,
    dsRepo: DataStoreRepository
) : ViewModel(), FileListViewModelLogics {

    private val selectMode = Variables.selectMode
    private val selectedItems = Variables.selectedItems

    private val uiStateTemp = combine(
        module.dirTree,
        module.notAccessible,
        module.currentDirectory.withPrevious_td(),
    ) {dirTree, notAccessible, lastKnownDirectory->
        FileListUiState(
            dirTree = dirTree.map { File(it) }.map { DirectoryHolderItem.fromFile(it) },
            accessFailText = notAccessible,
            lastKnownDirectory = lastKnownDirectory,
        )
    }

    override val uiState = combine(
        uiStateTemp,
        module.listFiles,
        selectMode,
        selectedItems,
        dsRepo.sortBy
    ){ state, listFiles, mode,items,sortMode ->
        FileListUiState(
            listFiles = sortLogic(listFiles, sortMode).map { FileHolderItem.fromFile(it,items) },
            dirTree = state.dirTree,
            emptyDirectoryText = listFiles.isEmpty(),
            accessFailText = state.accessFailText,
            lastKnownDirectory = state.lastKnownDirectory,
            selectMode = mode,
            selectedItems = items
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FileListUiState()
    )

    override fun onDirectoryClick(item: DirectoryHolderItem,mode:Int) {
        viewModelScope.launch {
            if(mode != MULTI_SELECT_MODE) {
                setCurrentDirectory(item.absolutePath)
            }
        }
    }

    override fun onFileClick(context: Context, item: FileHolderItem,mode:Int) {
        val selected = selectedItems.value
        viewModelScope.launch {
            if(mode == MULTI_SELECT_MODE){
                if(selected.contains(item.absolutePath)){
                    selectedItems.value = selected.toList().minus(item.absolutePath).toTypedArray()
                }else{
                    selectedItems.value = selected.toList().plus(item.absolutePath).toTypedArray()
                }
            }else {

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
    }

    override fun onFileLongClick(item: FileHolderItem,mode:Int) {
        when(mode){
            DEFAULT_MODE -> {
                selectMode.value = MULTI_SELECT_MODE
                selectedItems.value = arrayOf(item.absolutePath)
            }
        }
    }

    /** Todo: listFiles 정렬 로직 만들기 **/
    private fun sortLogic(array: Array<File>, sortMode: String?): List<File> {
        return when (sortMode) {
            "" -> array.sortedBy { it.name }
            else -> array.sortedBy { it.name }
        }
    }

    override fun onBackPressed(mode:Int) {
        if(mode != MULTI_SELECT_MODE ) {

            val parent = File(module.currentDirectory.value).parentFile
            viewModelScope.launch {
                parent?.let {
                    setCurrentDirectory(it.absolutePath)
                }
            }
        } else{
            selectMode.value = DEFAULT_MODE
        }
    }

    private suspend fun setCurrentDirectory(directory: String) =
        module.setCurrentDirectory(directory)

    /** 새 경로로 이동할 때, scroll 할 위치 가져오기 ( auto scroll 이 필요하다면 ) **/
    override fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int {
        return listFiles.map { it.absolutePath }.indexOf(lastKnownDirectory)
    }
//
//    fun getCurrentDirectory():File{
//        val result = File(uiState.value.dirTree.last().absolutePath)
//        println("getCurrentDirectory: ${result.absolutePath}")
//        return result
//    }

}

data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false,
    val lastKnownDirectory: String? = null,
    val selectMode:Int = DEFAULT_MODE,
    val selectedItems: Array<String> = emptyArray()
)