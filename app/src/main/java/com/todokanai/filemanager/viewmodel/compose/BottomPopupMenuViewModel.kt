package com.todokanai.filemanager.viewmodel.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Objects.modeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BottomPopupMenuViewModel @Inject constructor():ViewModel() {

    private val manager by lazy{modeManager}

    fun selected():Array<File>{
        return manager.selectedFiles.value
    }

    fun zip(){

    }

    fun selectAll(){
        viewModelScope.launch {
            manager.selectAll()
        }
    }

    fun unselectAll(){
        viewModelScope.launch {
            manager.clearSelectedFiles()
        }
    }
}