package com.todokanai.filemanager.viewmodel.compose.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Objects.modeManager
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InfoDialogViewModel @Inject constructor() : ViewModel() {

    /** number of selected files **/
    //val selectedNumberText = MutableStateFlow<String>(modeManager.selectedFiles.)
    val selectedNumberText = modeManager.selectedFiles.map{ files ->
        test(files.size)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = "0"
    )

    val sizeText = modeManager.selectedFiles.map { files ->
        files.getTotalSize_td().readableFileSize_td()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = "null"
    )


    //val sizeText = MutableStateFlow<String>("sizeText")

    private fun test(value:Int):String{
        val result = "$value 개"
        return result
    }

}