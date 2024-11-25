package com.todokanai.filemanager.abstracts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
/**
 * handles permissions
 * **/
abstract class BaseActivity: AppCompatActivity() {

    abstract val permissions:Array<String>
    abstract val requestCode:Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(permissions.isNotEmpty()){
            requestPermissions(permissions,requestCode)
        }
    }
}