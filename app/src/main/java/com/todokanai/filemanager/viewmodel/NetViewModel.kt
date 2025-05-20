package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.NetUiRepository
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(
    uiRepo: NetUiRepository,
    val module: NetFileModule,
    val ftpClient: FTPClient
) : ViewModel(), NetViewModelLogics {

    val uiState = combine(
        uiRepo.dirTreeNew,
        uiRepo.itemList,
    ) { dirTree, itemList ->
        NetUiState(
            dirTree = dirTree.map { DirectoryHolderItem.fromAbsolutePath(it) },
            itemList = itemList.map {
                FileHolderItem.fromFTPFile(it.first, it.second)
            },
            emptyDirectoryText = itemList.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetUiState()
    )

    //  데이터 흐름 방향이 위쪽 ( ViewModel -> UI )
    //---------------------------------------------------
    //  데이터 흐름 방향이 아래쪽 ( UI -> ViewModel )

    override fun onItemClick(item: FileHolderItem) {
        viewModelScope.launch {
            if (item.isDirectory) {
                setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a File")
            }
        }
    }

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        viewModelScope.launch {
            setCurrentDirectory(item.absolutePath)
        }
    }

    override fun toParent(onLogout: (Boolean) -> Unit) {
        viewModelScope.launch {
            val parent = getParent()
            if (parent == null) {
             //   withContext(Dispatchers.Default) {
                    module.logout()
                    withContext(Dispatchers.Main){
                        onLogout(ftpClient.isConnected)
                    }
             //   }
            } else {
                setCurrentDirectory(directory = parent)
            }
        }
    }

    private fun getParent(): String? = getParentAbsolutePath_td(module.currentDirectory.value)  // Todo: module.currentDirectory 가 여기서 보이는게 바람직한지?

    private suspend fun setCurrentDirectory(directory: String) = module.setCurrentDirectory(directory)
}

data class NetUiState(
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val itemList: List<FileHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false
)