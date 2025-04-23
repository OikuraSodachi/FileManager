package com.todokanai.filemanager.abstactlogics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class MainActivityLogics: AppCompatActivity() {

    abstract val binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareLateInit()
        handlePermission()
        prepareView()
        collectUiState()
        setContentView(binding.root)
    }

    abstract fun handlePermission()
    abstract fun prepareLateInit()
    abstract fun prepareView()
    abstract fun collectUiState()

}