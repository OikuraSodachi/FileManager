package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dsRepo: DataStoreRepository): ViewModel(){


}