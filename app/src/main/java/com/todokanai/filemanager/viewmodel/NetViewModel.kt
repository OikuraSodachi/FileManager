package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.tools.independent.NetFileModule
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(
    private val dsRepo: DataStoreRepository,
    val serverRepo:ServerInfoRepository,
    val module: NetFileModule
) : NetViewModelLogics() {

    private val _uiState = MutableStateFlow(NetUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dirTree.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        dirTree = it
                    )
                }
            }
        }
        viewModelScope.launch {
            itemList.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        itemList = it
                    )
                }
            }
        }
        viewModelScope.launch {
            serverListFlow.collect {
                _uiState.update { currentState ->
                    currentState.copy(serverList = it)
                }
            }
        }
    }

    override val dirTree: Flow<List<DirectoryHolderItem>>
        get() = module.currentDirectory.map {
            convertToDirTree(it)
        }

    override val itemList: Flow<List<FileHolderItem>>
        get() = module.currentDirectory.map { directory ->
            module.listFilesInFtpDirectory(
                dsRepo.getServerIp(),
                dsRepo.getUserId(),
                dsRepo.getUserPassword(),
                directory
            ).map {
                FileHolderItem.fromFTPFile(it, directory)
            }
        }

    override fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            module.login(
                server = dsRepo.getServerIp(),
                username = dsRepo.getUserId(),
                password = dsRepo.getUserPassword(),
                port = 21
            )
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
        val parent = getParentAbsolutePath_td(module.currentDirectory.value)
        parent?.let {
            viewModelScope.launch {
                module.setCurrentDirectory(directory = it)
            }
        }
    }

    fun startDownload(targetDirectory: File, targetFiles: Array<FileHolderItem>) {

    }


    override val serverListFlow: Flow<List<ServerHolderItem>> =
        serverRepo.serverInfoFlow.map { infoList ->
            infoList.map {
                ServerHolderItem(
                    name = it.name,
                    id = it.no!!    // Todo: NPE 발생 가능성 확인 필요
                )
            }
        }

    override fun onServerClick(server: ServerHolderItem) {

    }

    override fun deleteServer(server: ServerHolderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.deleteByIndex(server.id)
        }
    }

    override fun saveServerInfo(name: String, ip: String, id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            serverRepo.insert(name, ip, id, password)
        }
    }
}

data class NetUiState(
    val dirTree: List<DirectoryHolderItem> = emptyList(),
    val itemList: List<FileHolderItem> = emptyList(),
    val serverList: List<ServerHolderItem> = emptyList(),
    val loggedIn: Boolean = false
)