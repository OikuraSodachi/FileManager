package com.todokanai.filemanager.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** 주의: data layer 에서만 사용할 것 **/
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
