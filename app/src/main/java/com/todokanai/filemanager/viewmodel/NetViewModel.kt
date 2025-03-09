package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor() :ViewModel(){
    val itemFlow: Flow<List<File>> = emptyFlow()
    fun onItemClick(context: Context,item: File){}
}