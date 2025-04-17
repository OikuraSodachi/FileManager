package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.independent.NetFileModule
import com.todokanai.filemanager.tools.independent.getParentAbsolutePath_td
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(
    private val dsRepo: DataStoreRepository,
    val module: NetFileModule
) : NetViewModelLogics() {

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
            val ftpFile = module.fileInfo(item.absolutePath)

            if (ftpFile.isDirectory) {
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

    fun startDownload(targetDirectory: File,targetFiles:Array<FileHolderItem>){

    }
}