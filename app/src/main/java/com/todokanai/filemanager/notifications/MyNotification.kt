package com.todokanai.filemanager.notifications

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

/** 여기서 NotificationManager 가 보이면 안됨 **/
class MyNotification(@DrawableRes private val smallIcon: Int){

    fun basicNotification(
        context: Context,
        title: String?,
        message: String?,
        channelId:String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context,channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
    }
}