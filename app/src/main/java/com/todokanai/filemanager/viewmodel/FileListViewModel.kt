package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.openFileFromUri_td
import com.todokanai.filemanager.tools.independent.withPrevious_td
import com.todokanai.filemanager.viewmodel.logics.FileListViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    val module: FileModule,
    val dsRepo:DataStoreRepository
) : ViewModel(), FileListViewModelLogics {

    /** 경로 내 File 목록
     *
     * Todo: Sort 작업은 마지막에 한번만 하는 게 나을 듯? **/
    private val listFiles = combine(
        module.listFiles,
        dsRepo.sortBy
    ) { listFiles, mode ->
        listFileSorter(listFiles,mode)
    }

    private val dirTree = module.dirTree.map { tree ->
        tree.map {
            File(it)
        }
    }

    /** Todo: listFiles 정렬 로직 만들기 **/
    private fun listFileSorter(listFiles: Array<File>, sortMode: String?):List<File>{
        return when (sortMode) {
            "" -> listFiles.sortedBy{it.name}
            else -> listFiles.sortedBy{it.name}
        }
    }

    val uiState = combine(
        listFiles,
        dirTree,
        module.notAccessible,
        module.currentDirectory.withPrevious_td()
    ) { listFiles, dirTree, notAccessible, lastKnownDirectory ->
        FileListUiState(
            listFiles = listFiles.map { FileHolderItem.fromFile(it) },
            dirTree = dirTree.map { DirectoryHolderItem.fromFile(it) },
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

    fun onBackPressed() {
        val parent = File(module.currentDirectory.value).parentFile
        viewModelScope.launch {
            parent?.let {
                setCurrentDirectory(it.absolutePath)
            }
        }
    }

    private suspend fun setCurrentDirectory(directory:String) = module.setCurrentDirectory(directory)

    /** 새 경로로 이동할 때, scroll 할 위치 가져오기 ( auto scroll 이 필요하다면 ) **/
    fun scrollPosition(listFiles: List<FileHolderItem>, lastKnownDirectory: String?): Int {
        return listFiles.map { it.absolutePath }.indexOf(lastKnownDirectory)
    }

    fun popupMenuList(selected: Set<FileHolderItem>): List<Pair<String, () -> Unit>> {
        val result = mutableListOf<Pair<String, () -> Unit>>()
        result.run{
            add(Pair("Upload", { println("${selected.map { it.name }}") }))
            add(Pair("Zip",{}))
            add(Pair("Copy",{}))
            add(Pair("Info",{}))
            if(selected.size == 1){
                add(Pair("Rename",{}))
            }
        }
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