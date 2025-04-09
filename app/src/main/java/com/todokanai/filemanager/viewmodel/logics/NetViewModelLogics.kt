package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.launch

abstract class NetViewModelLogics() : ViewModel() {

    init{
        viewModelScope.launch {
            updateUI()
        }
    }

    abstract suspend fun updateUI()

    abstract fun onItemClick(item: FileHolderItem)

    abstract fun setCurrentDirectory(absolutePath:String)

    abstract fun toParent()

}