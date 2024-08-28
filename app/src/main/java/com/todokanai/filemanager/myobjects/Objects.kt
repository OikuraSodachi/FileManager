package com.todokanai.filemanager.myobjects

import android.os.Environment
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.providers.MainActivityProvider
import com.todokanai.filemanager.tools.SelectModeManager
import com.todokanai.filemanager.tools.independent.FileModule

object Objects {
    lateinit var packageName : String
    //lateinit var modeManager : SelectModeManager
    val modeManager = SelectModeManager()
    val fileModule = FileModule(Environment.getExternalStorageDirectory())
    lateinit var contextObjects: ContextObjects
    lateinit var mainActivityProvider: MainActivityProvider
    lateinit var myNoti : MyNotification
}