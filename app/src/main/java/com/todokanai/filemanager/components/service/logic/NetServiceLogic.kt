package com.todokanai.filemanager.components.service.logic

import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationCompat

abstract class NetServiceLogic(): Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = netNotification("title","message")
        startForeground(1,notification.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun netNotification(title:String,message:String):NotificationCompat.Builder
}