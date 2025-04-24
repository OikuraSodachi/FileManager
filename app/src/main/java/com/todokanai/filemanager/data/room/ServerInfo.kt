package com.todokanai.filemanager.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_server")
data class ServerInfo(
    @ColumnInfo val ip:String,
    @ColumnInfo val id:String,
    @ColumnInfo val password:String
){
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    var no:Long? = null
}
