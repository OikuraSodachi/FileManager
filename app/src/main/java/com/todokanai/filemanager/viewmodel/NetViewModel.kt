package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.abstracts.NetFileModuleLogics
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module: NetFileModuleLogics) : NetViewModelLogics() {

    private val _uiState = MutableStateFlow(NetUiState())
    val uiState: StateFlow<NetUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            itemList.collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        itemList = it
                    )
                }
            }
        }
    }


    override val itemList: Flow<List<FileHolderItem>>
        get() = combine(
            module.netItemList,
            module.currentDirectory
        ) { items, directory ->
            items.map {
                it.toFileHolderItem(directory)
            }
        }

    override fun onItemClick(item: FileHolderItem) {
        if (item.isDirectory) {
            setCurrentDirectory(item.absolutePath)
        } else {
            println("this is a File")
        }
    }

    override fun setCurrentDirectory(absolutePath: String) {
        module.setCurrentDirectory(absolutePath)
    }

    override fun toParent() {
        module.toParentDirectory()
    }



}

data class NetUiState(
    val itemList: List<FileHolderItem> = emptyList()
)