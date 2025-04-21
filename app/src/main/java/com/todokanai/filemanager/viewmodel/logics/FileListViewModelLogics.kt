package com.todokanai.filemanager.viewmodel.logics

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow

abstract class FileListViewModelLogics : ViewModel() {


//  val uiState : StateFlow<FileListUiState>
//      get() = _uiState
//
//      -> Refactoring 할 때, 편의성 면에서 asStateFlow() 방식이 더 좋은듯

    abstract val dirTree: Flow<List<DirectoryHolderItem>>

    abstract val fileHolderList: Flow<List<FileHolderItem>>

    abstract val notAccessible: Flow<Boolean>

    abstract fun onDirectoryClick(item: DirectoryHolderItem)

    abstract fun onFileClick(context: Context, item: FileHolderItem)

    abstract fun setCurrentDirectory(directory: String)

}
