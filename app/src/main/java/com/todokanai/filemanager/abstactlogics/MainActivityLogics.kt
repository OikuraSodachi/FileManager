package com.todokanai.filemanager.abstactlogics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class MainActivityLogics: AppCompatActivity() {

    abstract val binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareLateInit()
        handlePermission()
        prepareView()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                collectUIState()
            }
        }
        setContentView(binding.root)
    }

    abstract fun handlePermission()
    abstract fun prepareLateInit()
    abstract fun prepareView()
    abstract suspend fun collectUIState()

}