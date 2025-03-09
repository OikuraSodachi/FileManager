package com.todokanai.filemanager.di

import android.os.Environment
import com.todokanai.filemanager.tools.independent.FileModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Etc {

    @Singleton
    @Provides
    fun provideFileModule(): FileModule {
        return FileModule(Environment.getExternalStorageDirectory())
    }

    @Singleton
    @Provides
    fun provideFTPClient():FTPClient{
        return FTPClient()
    }
}