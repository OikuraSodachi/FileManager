package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.viewmodel.basemodel.BaseNetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module:NetFileModule) : BaseNetViewModel(){


    private val _uiStateNew = MutableStateFlow(NetUiState())
    val uiState: StateFlow<NetUiState>
        get() = _uiStateNew

    init {
        viewModelScope.launch {
            module.temp.collect {
                _uiStateNew.update { currentState ->
                    currentState.copy(
                        itemList = it
                    )
                }
            }
        }
    }

    override fun onItemClick(item: FileHolderItem){
        if(item.isDirectory()) {
            setCurrentDirectory(item.absolutePath)
        }else{
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