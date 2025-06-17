package com.todokanai.filemanager.components.service

import android.app.NotificationChannel
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.todokanai.filemanager.R
import com.todokanai.filemanager.components.service.logic.NetServiceLogic
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.notifications.MyNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetService : NetServiceLogic() {
    companion object {
        lateinit var currentServer: ServerInfo
    }
    private lateinit var serviceChannel : NotificationChannel

//    @Inject
//    lateinit var netModule:NetFileModule
    private val myNoti by lazy{ MyNotification(R.drawable.ic_launcher_background) }


    override fun onBind(p0: Intent?): IBinder? {
        return Binder()
    }

    override fun onCreate() {
        super.onCreate()
        println("netService: onCreate")
    }

    override fun netNotification(title: String, message: String): NotificationCompat.Builder {
        return myNoti.basicNotification(this, title, message,serviceChannel.id)
    }

}