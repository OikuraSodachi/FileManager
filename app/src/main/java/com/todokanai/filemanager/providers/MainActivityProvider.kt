package com.todokanai.filemanager.providers

import android.app.Activity

class MainActivityProvider(activity: Activity) {

    val mainActivity by lazy{activity}

}