package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.room.ServerInfo
import com.todokanai.filemanager.data.room.ServerInfoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerInfoRepository @Inject constructor(private val serverInfoDao:ServerInfoDao){

    val serverInfoFlow = serverInfoDao.getAll()

    suspend fun insert(serverInfo: ServerInfo) = serverInfoDao.insert(serverInfo)

    suspend fun delete(serverInfo: ServerInfo) = serverInfoDao.delete(serverInfo)

    suspend fun deleteAll() = serverInfoDao.deleteAll()
}