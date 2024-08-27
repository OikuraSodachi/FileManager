package com.todokanai.filemanager.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.todokanai.filemanager.R
import com.todokanai.filemanager.myobjects.Constants.CHANNEL_ID

class MyNotification(appContext: Context) {

    private val context by lazy{appContext}
    private val channelId = CHANNEL_ID
    private val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_LOW).apply {
        description = "This is my notification channel"
    }

    private val icon by lazy{ R.drawable.ic_launcher_background}

    private val notificationManager by lazy{ context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager}

    fun sendStringNotification(title: String, message: String){

        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td()

    }

    fun sendSilentNotification(builder:NotificationCompat.Builder){
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .launch_td()
    }


    /** send notification  **/
    private fun NotificationCompat.Builder.launch_td(){
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