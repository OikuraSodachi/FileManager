package com.todokanai.filemanager.viewmodel

import com.todokanai.filemanager.abstracts.NetFileModuleLogics
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.viewmodel.logics.NetUiState
import com.todokanai.filemanager.viewmodel.logics.NetViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module: NetFileModuleLogics) : NetViewModelLogics(){

    private val _uiState = MutableStateFlow(NetUiState())
    val uiState: StateFlow<NetUiState>
        get() = _uiState

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

    override suspend fun updateUI(){
        module.netItemList.collect {
            _uiState.update { currentState ->
                currentState.copy(
                    itemList = it
                )
            }
        }
    }

}