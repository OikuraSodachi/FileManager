package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.repository.NetUiRepository
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(
    uiRepo: NetUiRepository,
    val serverRepo: ServerInfoRepository,
    val module: NetFileModule
) : ViewModel(), NetViewModelLogics {

    val uiState = combine(
        uiRepo.dirTreeNew,
        uiRepo.itemList,
        uiRepo.serverListFlow,
        uiRepo.loggedIn
    ) { dirTree, itemList, serverList, loggedIn ->
        NetUiState(
            dirTree = dirTree.map { DirectoryHolderItem.fromAbsolutePath(it) },
            itemList = itemList.map {
                FileHolderItem.fromFTPFile(it.first, it.second)
            },
            emptyDirectoryText = itemList.isEmpty(),
            serverList = serverList.map {
                ServerHolderItem(
                    it.name,
                    it.no!!       // Todo: NPE 발생 가능성 확인 필요
                )
            },
            loggedIn = loggedIn
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetUiState()
    )

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

    override fun toParent() {
        viewModelScope.launch {
            val parent =
                getParentAbsolutePath_td(module.currentDirectory.value)  // Todo: module.currentDirectory 가 여기서 보이는게 바람직한지?
            if (parent == null) {
                logout()
            } else {
                setCurrentDirectory(directory = parent)
            }
        }
    }

    override fun onServerClick(server: ServerHolderItem) {
        viewModelScope.launch(Dispatchers.Default) {
            module.login(serverRepo.getById(id = server.id))
        }
    }

    override fun deleteServer(server: ServerHolderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.deleteByIndex(server.id)
        }
    }

    override fun saveServerInfo(name: String, ip: String, id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.insert(ServerInfo(name, ip, id, password))
        }
    }

    private suspend fun setCurrentDirectory(directory: String) = module.setCurrentDirectory(directory)

    fun logout() {
        viewModelScope.launch {
            module.logout()
        }
    }
}

data class NetUiState(
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val itemList: List<FileHolderItem> = emptyList(),
    val emptyDirectoryText: Boolean = false,
    val serverList: List<ServerHolderItem> = emptyList(),
    val loggedIn: Boolean = false
)