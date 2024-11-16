package com.todokanai.filemanager.abstracts

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity: AppCompatActivity() {

    /** MANAGE_EXTERNAL_STORAGE 등 특수 권한은 해당되지 않음 **/
    abstract val permissions:Array<String>
    abstract val requestCode:Int

    /** onBackPressed를 override 할지 여부 **/
    abstract val backPressedOverride:Boolean

    abstract fun onBackPressedOverride()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(permissions.isNotEmpty()){
            requestPermissions(permissions,requestCode)
        }
        if(backPressedOverride) {
            onBackPressedDispatcher.addCallback {
                onBackPressedOverride()
            }
        }
    }
}