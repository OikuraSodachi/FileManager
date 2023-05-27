package com.todokanai.filemanager.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.todokanai.filemanager.databinding.ActivityMainBinding
import com.todokanai.filemanager.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val viewModel : MainViewModel by viewModels()
    private lateinit var activityResult: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!viewModel.checkPermission(this)) {
            viewModel.requestPermission(this)
        }

        activityResult =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted)
                    finish()
            }

        /*

        activityResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        activityResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        
         */

        viewModel.requestStorageManageAccess(this)
    }
}