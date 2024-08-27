package com.todokanai.filemanager.myobjects

import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.providers.MainActivityProvider
import com.todokanai.filemanager.tools.SelectModeManager
import com.todokanai.filemanager.viewmodel.submodel.FileListModel

object Objects {
    lateinit var packageName : String
    //lateinit var modeManager : SelectModeManager
    val modeManager = SelectModeManager()
    lateinit var fileModel: FileListModel
    lateinit var contextObjects: ContextObjects
    lateinit var mainActivityProvider: MainActivityProvider
    lateinit var myNoti : MyNotification
}