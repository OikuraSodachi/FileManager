package com.todokanai.filemanager.viewmodel.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Objects.mainActivityProvider
import com.todokanai.filemanager.tools.independent.exit_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuBtnViewModel @Inject constructor():ViewModel(){

    private val activityProvider by lazy{mainActivityProvider}

    /** exit_td(activity) **/
    fun exit() {
        viewModelScope.launch {
            exit_td(activityProvider.mainActivity)
        }
    }




}