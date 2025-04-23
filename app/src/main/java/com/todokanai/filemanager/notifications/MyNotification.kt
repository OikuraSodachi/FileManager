package com.todokanai.filemanager.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.todokanai.filemanager.R
import com.todokanai.filemanager.myobjects.Constants

/** Todo: 추상화 적용할 것 **/
class MyNotification(val manager: NotificationManager) {

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

    fun sendSilentNotification(context:Context,title: String?, message: String?) {
        NotificationCompat.Builder(context, silentChannel.id)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .launch_td(silentChannel)
    }


//    fun sendCompletedNotification(title: String?, message: String?) {
//        NotificationCompat.Builder(context, completedChannel.id)
//            .setSmallIcon(icon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .launch_td(completedChannel)
//    }
//
//    fun sendProgressNotification(title: String?, message: String?, progress: Int) {
//        NotificationCompat.Builder(context, silentChannel.id)
//            .setSmallIcon(icon)
//            .setContentTitle(title)
//            .setContentText(message)
//            // .setOngoing(true)
//            .setProgress(100, progress, false)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .launch_td(silentChannel)
//    }



    fun ongoingNotiTest(context: Context) = Notification.Builder(context, silentChannel.id)
        .setSmallIcon(icon)
        .setPriority(Notification.PRIORITY_DEFAULT)
        .setOngoing(true)

    /** send notification  **/
    private fun NotificationCompat.Builder.launch_td(channel: NotificationChannel) {
        manager.createNotificationChannel(channel)
        manager.notify(0, this@launch_td.build())
    }
}