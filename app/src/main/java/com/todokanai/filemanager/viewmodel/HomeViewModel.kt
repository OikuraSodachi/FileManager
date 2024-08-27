package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.tools.independent.ToastShort_td
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor():ViewModel() {

    fun testButton(context: Context){
        ToastShort_td(context,"TestButton")

    }

}