package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.actions.CopyAction
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

    override val uiState = combine(
        module.listFiles,
        module.dirTree,
        module.notAccessible,
        module.currentDirectory.withPrevious_td(),
        dsRepo.sortBy
    ) { listFiles, dirTree, notAccessible, lastKnownDirectory, sortMode ->
        FileListUiState(
            listFiles = sortLogic(listFiles, sortMode).map { FileHolderItem.fromFile(it) },
            dirTree = dirTree.map { File(it) }.map { DirectoryHolderItem.fromFile(it) },
            emptyDirectoryText = listFiles.isEmpty(),
            accessFailText = notAccessible,
            lastKnownDirectory = lastKnownDirectory
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FileListUiState()
    )

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        viewModelScope.launch {
            setCurrentDirectory(item.absolutePath)
        }
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

    /** Todo: listFiles 정렬 로직 만들기 **/
    private fun sortLogic(array: Array<File>, sortMode: String?): List<File> {
        return when (sortMode) {
            "" -> array.sortedBy { it.name }
            else -> array.sortedBy { it.name }
        }
    }

    override fun onBackPressed() {
        val parent = File(module.currentDirectory.value).parentFile
        viewModelScope.launch {
            parent?.let {
                setCurrentDirectory(it.absolutePath)
            }
        }
    }

    private suspend fun setCurrentDirectory(directory: String) =
        module.setCurrentDirectory(directory)

    /** 새 경로로 이동할 때, scroll 할 위치 가져오기 ( auto scroll 이 필요하다면 ) **/
    override fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int {
        return listFiles.map { it.absolutePath }.indexOf(lastKnownDirectory)
    }

    override fun popupMenuList(selected: Array<FileHolderItem>): List<Pair<String, () -> Unit>> {
        val result = mutableListOf<Pair<String, () -> Unit>>()
        val files = selected.map{it.absolutePath}
        result.run {
            add(Pair("Upload", { println("${selected.map { it.name }}") }))
            add(Pair("Zip", {}))
            add(Pair("Copy", { CopyAction(selectedFiles = files.toTypedArray(), targetDirectory = getCurrentDirectory()) }))
            add(Pair("Info", {}))
            if (selected.size == 1) {
                add(Pair("Rename", {}))
            }
        }
        return result
    }

    fun getCurrentDirectory():String{
        TODO()
    }
}

data class FileListUiState(
    val listFiles: List<FileHolderItem> = emptyList(),
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val accessFailText: Boolean = false,
    val lastKnownDirectory: String? = null
)