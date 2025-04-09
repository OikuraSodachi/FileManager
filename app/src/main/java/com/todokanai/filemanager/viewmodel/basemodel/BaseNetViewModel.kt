package com.todokanai.filemanager.viewmodel.basemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.launch

abstract class BaseNetViewModel() : ViewModel() {

    init{
        onInit()
    }

    private fun onInit(){
        viewModelScope.launch {
            updateUI()
        }
    }

    abstract suspend fun updateUI()

    abstract fun onItemClick(item: FileHolderItem)

    abstract fun setCurrentDirectory(absolutePath:String)

    abstract fun toParent()

}

data class NetUiState(
    val itemList: List<FileHolderItem> = emptyList()
)