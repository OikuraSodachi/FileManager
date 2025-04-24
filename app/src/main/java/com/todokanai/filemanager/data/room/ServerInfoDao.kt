package com.todokanai.filemanager.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerInfoDao {
    @Query("select * from room_server")
    fun getAll(): Flow<List<ServerInfo>>

    @Query("select * from room_server")
    suspend fun getAllNonFlow(): List<ServerInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(serverInfo: ServerInfo)

    @Delete
    suspend fun delete(serverInfo: ServerInfo)

    @Query("delete from room_server where `no`=:no")
    suspend fun deleteByIndex(no: Long)

    @Query("delete from room_server")
    suspend fun deleteAll()

    @Query("select * from room_server where `no`=:no")
    fun getServerByIndex(no: Long): Flow<ServerInfo>
}