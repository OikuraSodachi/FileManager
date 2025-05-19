package com.todokanai.filemanager.components.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.todokanai.filemanager.components.service.logic.NetServiceLogic
import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.notifications.MyNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetService : NetServiceLogic() {
    companion object {
        lateinit var currentServer: ServerInfo
    }

//    @Inject
//    lateinit var netModule:NetFileModule

    @Inject
    lateinit var myNoti: MyNotification

    override fun onBind(p0: Intent?): IBinder? {
        return Binder()
    }

    override fun onCreate() {
        super.onCreate()
        println("netService: onCreate")
    }

    override fun netNotification(title: String, message: String): NotificationCompat.Builder {
        return myNoti.basicNotification(this,title,message)
    }

}