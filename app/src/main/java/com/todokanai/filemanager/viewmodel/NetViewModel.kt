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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(
    val serverRepo: ServerInfoRepository,
    val uiRepo: NetUiRepository,
    val module: NetFileModule
) : ViewModel(), NetViewModelLogics {

    private val _uiState = MutableStateFlow(NetUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            uiRepo.dirTreeNew.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        dirTree = it.map { DirectoryHolderItem.fromAbsolutePath(it) }
                    )
                }
            }
        }
        viewModelScope.launch {
            uiRepo.itemList.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        itemList = it.map {
                            FileHolderItem.fromFTPFile(it.first, it.second)
                        }
                    )
                }
            }
        }
        viewModelScope.launch {
            uiRepo.serverListFlow.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        serverList = it.map {
                            ServerHolderItem(it.name, it.no!!) // Todo: NPE 발생 가능성 확인 필요
                        }
                    )
                }
            }
        }
        viewModelScope.launch {
            uiRepo.loggedIn.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        loggedIn = it
                    )
                }
            }
        }
    }

    override fun onItemClick(item: FileHolderItem) {
        viewModelScope.launch {
            if (item.isDirectory) {
                module.setCurrentDirectory(item.absolutePath)
            } else {
                println("this is a File")
            }
        }
    }

    override fun onDirectoryClick(item: DirectoryHolderItem) {
        viewModelScope.launch {
            module.setCurrentDirectory(item.absolutePath)
        }
    }

    override fun toParent() {
        viewModelScope.launch {
            val parent =
                getParentAbsolutePath_td(module.currentDirectory.value)  // Todo: module.currentDirectory 가 여기서 보이는게 바람직한지?
            if (parent == null) {
                logout()
            } else {
                module.setCurrentDirectory(directory = parent)
            }
        }
    }

    override fun onServerClick(server: ServerHolderItem) {
        viewModelScope.launch(Dispatchers.Default) {
            module.loginWrapper(serverRepo.getById(id = server.id))
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

    fun logout() {
        // Todo: logout 동작
    }
}

data class NetUiState(
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val itemList: List<FileHolderItem> = emptyList(),
    val serverList: List<ServerHolderItem> = emptyList(),
    val loggedIn: Boolean = false
)