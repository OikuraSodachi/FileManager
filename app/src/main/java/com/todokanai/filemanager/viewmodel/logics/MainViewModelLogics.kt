package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class MainViewModelLogics:ViewModel() {

    abstract val fragCode: Flow<Int>
}