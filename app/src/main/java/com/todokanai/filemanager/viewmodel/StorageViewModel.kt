package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.myobjects.Variables
import com.todokanai.filemanager.tools.independent.FileModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(val module:FileModule) : ViewModel(){

    val storageHolderList = Variables.storages.map{ it.toList() }

    fun onItemClick(file:File) = module.updateCurrentPath(file.absolutePath)
}