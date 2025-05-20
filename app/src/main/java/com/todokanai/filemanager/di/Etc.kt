package com.todokanai.filemanager.di

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import androidx.work.WorkManager
import com.todokanai.filemanager.notifications.MyNotification
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.repository.FileListUiRepository
import com.todokanai.filemanager.repository.NetUiRepository
import com.todokanai.filemanager.repository.ServerInfoRepository
import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.NetFileModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Etc {

    @Singleton
    @Provides
    fun provideFileModule(): FileModule {
        return FileModule(Dispatchers.IO, Environment.getExternalStorageDirectory().absolutePath)
    }

    @Singleton
    @Provides
    fun provideNetFileModule(ftpClient: FTPClient): NetFileModule {
        return NetFileModule(Dispatchers.Default, ftpClient,defaultPath = "")
    }

    @Provides
    fun provideMyNotification(notificationManager: NotificationManager): MyNotification {
        return MyNotification(notificationManager)
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideNetUiRepository(
        netFileModule: NetFileModule,
        serverRepository: ServerInfoRepository
    ): NetUiRepository {
        return NetUiRepository(netFileModule, serverRepository)
    }

    @Provides
    fun provideFileListUiRepository(
        fileModule: FileModule,
        dataStoreRepository: DataStoreRepository
    ): FileListUiRepository {
        return FileListUiRepository(fileModule, dataStoreRepository)
    }

    @Singleton
    @Provides
    /** 동시에 한 개의 서버에만 접속하는 상황을 전제로 선언했음. **/
    fun provideFTPClient():FTPClient{
        return FTPClient()
    }
}