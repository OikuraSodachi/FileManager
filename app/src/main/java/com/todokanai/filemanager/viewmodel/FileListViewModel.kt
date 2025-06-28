package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.myobjects.Constants.BY_DATE_ASCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_DATE_DESCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_NAME_ASCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_NAME_DESCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_SIZE_ASCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_SIZE_DESCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_TYPE_ASCENDING
import com.todokanai.filemanager.myobjects.Constants.BY_TYPE_DESCENDING
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

    private val modeManager = Variables.selectModeManager
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
        modeManager.selectMode,
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

    private fun currentMode() = modeManager.currentMode()

    override fun refresh(currentDirectory: String){
        viewModelScope.launch {
            setCurrentDirectory(currentDirectory)
        }
    }

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        val mode = currentMode()
        viewModelScope.launch {
            if(mode != MULTI_SELECT_MODE) {
                setCurrentDirectory(item.absolutePath)
            }
        }
    }

    override fun onFileClick(context: Context, item: FileHolderItem) {
        val selected = selectedItems.value
        val mode = currentMode()
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

    override fun onFileLongClick(item: FileHolderItem) {
        val mode = currentMode()
        when(mode){
            DEFAULT_MODE -> {
                modeManager.toMultiSelectMode()
                selectedItems.value = arrayOf(item.absolutePath)
            }
        }
    }

    private fun sortLogic(array: Array<File>, sortMode: String?): List<File> {
        return when (sortMode) {
            BY_NAME_ASCENDING -> array.sortedBy { it.name }
            BY_NAME_DESCENDING -> array.sortedByDescending { it.name }
            BY_SIZE_ASCENDING -> array.sortedBy { it.length() }
            BY_SIZE_DESCENDING -> array.sortedByDescending { it.length() }
            BY_DATE_ASCENDING -> array.sortedBy { it.lastModified() }
            BY_DATE_DESCENDING -> array.sortedByDescending { it.lastModified() }
            BY_TYPE_ASCENDING -> array.sortedBy { it.extension }
            BY_TYPE_DESCENDING -> array.sortedByDescending { it.extension }
            else -> array.sortedBy { it.name }
        }.sortedWith(compareByDescending{ it.isDirectory })     // Directory 를 앞쪽으로 정렬
    }

    override fun onBackPressed(currentDirectory: String) {
        val mode = currentMode()

        if(mode != MULTI_SELECT_MODE ) {

            val parent = File(currentDirectory).parentFile
            viewModelScope.launch {
                parent?.let {
                    setCurrentDirectory(it.absolutePath)
                }
            }
        } else{
            modeManager.toDefaultMode()
        }
    }

    private suspend fun setCurrentDirectory(directory: String) =
        module.setCurrentDirectory(directory)

    /** 새 경로로 이동할 때, scroll 할 위치 가져오기 ( auto scroll 이 필요하다면 ) **/
    override fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int {
        return listFiles.map { it.absolutePath }.indexOf(lastKnownDirectory)
    }

    override fun renameFile(file: File, newName: String) {
        file.renameTo(File(file.parentFile, newName))
        modeManager.toDefaultMode()
    }

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