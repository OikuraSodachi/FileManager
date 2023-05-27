package com.todokanai.filemanager.di

import android.content.Context
import com.todokanai.filemanager.room.MyDatabase
import com.todokanai.filemanager.room.UserDao
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

    @Provides
    fun provideUserDao(myDatabase: MyDatabase) : UserDao {
        return myDatabase.userDao()
    }

}