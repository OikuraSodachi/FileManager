package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.launch
import java.io.File

abstract class FileListViewModelLogics: ViewModel() {

    init{
        viewModelScope.launch {
            updateUI()
        }
    }

    abstract suspend fun updateUI()

    abstract fun onDirectoryClick(file: File)

    abstract fun onFileClick(context: Context, item: FileHolderItem)

}