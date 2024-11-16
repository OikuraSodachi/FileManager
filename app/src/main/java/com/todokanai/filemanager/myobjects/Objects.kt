package com.todokanai.filemanager.myobjects

import android.Manifest
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.tools.SelectModeManager

object Objects {
    lateinit var packageName : String
    val modeManager = SelectModeManager()
    lateinit var contextObjects: ContextObjects
    lateinit var myNoti : MyNotification
    val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
}