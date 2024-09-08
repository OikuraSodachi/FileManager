package com.todokanai.filemanager.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW
import com.todokanai.filemanager.R
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Constants.CHANNEL_ID

class MyNotification(appContext: Context) {

    private val context by lazy{appContext}
    private val channelId = CHANNEL_ID
    private val cChannelId = Constants.NOTIFICATION_CHANNEL_ID_COMPLETED
    private val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_LOW).apply {
        description = "This is my notification channel"
    }

    private val completedChannel = NotificationChannel(cChannelId,"My Channel", NotificationManager.IMPORTANCE_DEFAULT)

    private val icon by lazy{ R.drawable.ic_launcher_background}

    private val notificationManager by lazy{ context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager}

    fun sendStringNotification(title: String?, message: String?,isSilent:Boolean=true){

        val test = if(isSilent){
            channel
        } else{
            completedChannel
        }

        val importance = if(isSilent){
            IMPORTANCE_LOW
        }else{
            IMPORTANCE_DEFAULT
        }

        test.importance = importance
        NotificationCompat.Builder(context, test.id)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td(test)

    }

    /*
    fun sendSilentNotification(builder:NotificationCompat.Builder){
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .launch_td()
    }

     */

    val ongoingNotiTest = Notification.Builder(context, channelId)
        .setSmallIcon(icon)
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setOngoing(true)



    fun defaultNotificationTest():Notification{
        val out = Notification.Builder(context, channelId)
            .setSmallIcon(icon)
            .setPriority(Notification.PRIORITY_DEFAULT)
            //.build()
        return out.build()
    }

    /** send notification  **/
    private fun NotificationCompat.Builder.launch_td(channel: NotificationChannel){
        notificationManager.createNotificationChannel(channel)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(0, this@launch_td.build())
        }
    }
}