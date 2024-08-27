package com.todokanai.filemanager.di

import android.content.Context
import androidx.work.WorkManager
import com.todokanai.filemanager.data.datastore.MyDataStore
import com.todokanai.filemanager.data.room.MyDatabase
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
    fun provideMyDatabase(@ApplicationContext context:Context):MyDatabase{
        return MyDatabase.getInstance(context)
    }

    @Provides
    fun providesMyDataStore(@ApplicationContext context: Context):MyDataStore{
        return MyDataStore(context)
    }

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager{
        return WorkManager.getInstance(context)
    }

}