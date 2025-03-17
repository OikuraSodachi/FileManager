package com.todokanai.filemanager.myobjects

import android.Manifest
import com.todokanai.filemanager.notifications.MyNotification

object Objects {
    lateinit var myNoti : MyNotification
    val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
}