package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    fun deleteServer(server:ServerHolderItem) {

    }

    fun onServerClick(context: Context, server:ServerHolderItem) {

    }
}