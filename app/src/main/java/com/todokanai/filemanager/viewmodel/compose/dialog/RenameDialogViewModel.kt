package com.todokanai.filemanager.viewmodel.compose.dialog

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.myobjects.Objects.fileModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RenameDialogViewModel @Inject constructor() :ViewModel(){

    private val model by lazy{fileModel}

    fun onConfirm(text:String){

    }

}