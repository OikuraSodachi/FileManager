package com.todokanai.filemanager.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.todokanai.filemanager.application.MyApplication
import com.todokanai.filemanager.myobjects.Constants.CHANNEL_ID

/**
 *  이 클래스의 method는 독립적으로 사용 가능함
 *
 */

class MyNotification(val channelId:String,val icon:Int) {

    val context = MyApplication.appContext

    fun createNotification(title:String,message:String) {

        // Notification 채널 생성
        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotiEnabled = notificationManager.areNotificationsEnabled()
        if(isNotiEnabled) {
            // Notification 채널 등록
            notificationManager.createNotificationChannel(channel)

            // NotificationCompat.Builder를 사용하여 Notification 생성
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Notification 표시
            with(NotificationManagerCompat.from(context)) {

                notify(0, builder.build())
            }
        }
    }

    fun deleteProgressNoti(progress:Int) {
        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle("deleting")
            .setContentText("deleting")
            .setOngoing(true)
            .setProgress(100,progress,false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, builder.build())
                return
            }
            notify(0, builder.build())
        }
    }

    fun copyProgressNoti(progress:Int){

            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is my notification channel"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle("copying")
                .setOngoing(true)
                .setContentText("copying")
                .setProgress(100, progress, false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notify(0, builder.build())
                }
                notify(0, builder.build())
            }
    }

    fun progressNoti(title: String,message: String,progress:Int){

        val channel = NotificationChannel(
            channelId,
            "My Channel",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(message)
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, builder.build())
            }
            notify(0, builder.build())
        }
    }

}