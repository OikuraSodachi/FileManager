package com.todokanai.filemanager.di

import com.todokanai.filemanager.tools.FileModule
import com.todokanai.filemanager.tools.NetFileModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        return FileModule(Dispatchers.IO)
    }

    @Singleton
    @Provides
    fun provideNetFileModule(ftpClient: FTPClient): NetFileModule {
        return NetFileModule(Dispatchers.Default, ftpClient)
    }

//    @Provides
//    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
//        return WorkManager.getInstance(context)
//    }

    @Singleton
    @Provides
            /** 동시에 한 개의 서버에만 접속하는 상황을 전제로 선언했음. **/
    fun provideFTPClient(): FTPClient {
        return FTPClient()
    }
}