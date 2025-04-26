package com.todokanai.filemanager.di

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import androidx.work.WorkManager
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.NetFileModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Etc {

    @Singleton
    @Provides
    fun provideFileModule(): FileModule {
        return FileModule(Environment.getExternalStorageDirectory().absolutePath)
    }

    @Singleton
    @Provides
    fun provideNetFileModule(): NetFileModule {
        return NetFileModule()
    }

    @Provides
    fun provideMyNotification(notificationManager: NotificationManager): MyNotification {
        return MyNotification(notificationManager)
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}