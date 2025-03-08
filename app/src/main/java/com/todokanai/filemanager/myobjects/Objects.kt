package com.todokanai.filemanager.myobjects

import android.Manifest
import com.todokanai.filemanager.notifications.MyNotification

object Objects {
    lateinit var packageName : String
    lateinit var contextObjects: ContextObjects
    lateinit var myNoti : MyNotification
    val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
}