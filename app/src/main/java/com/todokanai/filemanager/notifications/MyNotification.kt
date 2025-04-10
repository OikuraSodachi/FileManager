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
import com.todokanai.filemanager.R
import com.todokanai.filemanager.myobjects.Constants

/** Todo: 추상화 적용할 것 **/
class MyNotification(context: Context) {

    private val context by lazy { context }
    private val silentChannel = NotificationChannel(
        Constants.CHANNEL_ID,
        "My Channel",
        NotificationManager.IMPORTANCE_LOW
    ).apply {
        description = "This is my notification channel"
    }
    private val completedChannel = NotificationChannel(
        Constants.NOTIFICATION_CHANNEL_ID_COMPLETED,
        "My Channel",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    private val icon by lazy { R.drawable.ic_launcher_background }
    private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    fun sendSilentNotification(title: String?, message: String?) {
        NotificationCompat.Builder(context, silentChannel.id)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td(silentChannel)
    }


    fun sendCompletedNotification(title: String?, message: String?) {
        NotificationCompat.Builder(context, completedChannel.id)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td(completedChannel)
    }
    /*
    fun sendSilentNotification(builder:NotificationCompat.Builder){
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .launch_td()
    }
     */

    fun sendProgressNotification(title: String?, message: String?, progress: Int) {
        NotificationCompat.Builder(context, silentChannel.id)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            // .setOngoing(true)
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td(silentChannel)
    }

    val ongoingNotiTest = Notification.Builder(context, silentChannel.id)
        .setSmallIcon(icon)
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setOngoing(true)

    fun defaultNotificationTest(): Notification {
        val out = Notification.Builder(context, silentChannel.id)
            .setSmallIcon(icon)
            .setPriority(Notification.PRIORITY_DEFAULT)
        //.build()
        return out.build()
    }

    /** send notification  **/
    private fun NotificationCompat.Builder.launch_td(channel: NotificationChannel) {
        notificationManager.createNotificationChannel(channel)
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(0, this@launch_td.build())
        }
    }
}