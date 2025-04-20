package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class MainViewModelLogics:ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()
}

data class MainActivityUiState(
    val fragCode:Int = 1
)