package com.todokanai.filemanager.viewmodel.compose.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortDialogViewModel @Inject constructor(private val dsRepo:DataStoreRepository) : ViewModel() {

    val selectedItem : StateFlow<String?> = dsRepo.sortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5),
        initialValue = null
    )

    fun saveByDefault() = saveSortBy(Constants.BY_DEFAULT)
    fun saveByNameAscending() = saveSortBy(Constants.BY_NAME_ASCENDING)
    fun saveByNameDescending() = saveSortBy(Constants.BY_NAME_DESCENDING)
    fun saveBySizeAscending() = saveSortBy(Constants.BY_SIZE_ASCENDING)
    fun saveBySizeDescending() = saveSortBy(Constants.BY_SIZE_DESCENDING)
    fun saveByTypeAscending() = saveSortBy(Constants.BY_TYPE_ASCENDING)
    fun saveByTypeDescending() = saveSortBy(Constants.BY_TYPE_DESCENDING)
    fun saveByDateAscending() = saveSortBy(Constants.BY_DATE_ASCENDING)
    fun saveByDateDescending() = saveSortBy(Constants.BY_DATE_DESCENDING)

    private fun saveSortBy(value:String) {
        CoroutineScope(Dispatchers.IO).launch {
            dsRepo.saveSortBy(value)
        }
    }
}