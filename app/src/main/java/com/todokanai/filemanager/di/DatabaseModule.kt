package com.todokanai.filemanager.di

import android.content.Context
import androidx.work.WorkManager
import com.todokanai.filemanager.data.room.MyDatabase
import com.todokanai.filemanager.data.room.ServerInfoDao
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.repository.ServerInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return MyDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepository(context)
    }

    @Provides
    fun provideServerInfoDao(myDatabase: MyDatabase): ServerInfoDao {
        return myDatabase.serverDao()
    }

    @Singleton
    @Provides
    fun provideServerInfoRepository(serverInfoDao: ServerInfoDao): ServerInfoRepository {
        return ServerInfoRepository(serverInfoDao)
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

}