package com.todokanai.filemanager.myobjects

import com.todokanai.filemanager.myobjects.Variables.Companion.defaultStorage
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.tools.SelectModeManager
import com.todokanai.filemanager.tools.independent.FileModule

object Objects {
    lateinit var packageName : String
    val modeManager = SelectModeManager()
    val fileModule = FileModule(defaultStorage)
    lateinit var contextObjects: ContextObjects
    lateinit var myNoti : MyNotification
}