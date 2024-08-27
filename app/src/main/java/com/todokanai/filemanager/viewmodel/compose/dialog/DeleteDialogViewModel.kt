package com.todokanai.filemanager.viewmodel.compose.dialog

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.myobjects.Objects.fileModel
import com.todokanai.filemanager.myobjects.Objects.modeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor():ViewModel() {

    private val model by lazy{fileModel}
    private val manager by lazy{modeManager}


    fun delete(){
        val selectedFiles = manager.selectedFiles.value
        model.deleteFiles(selectedFiles)

    }

}