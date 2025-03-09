package com.todokanai.filemanager.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.todokanai.filemanager.abstracts.MyDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(appContext: Context): MyDataStore(appContext){

    companion object{
        val DATASTORE_SORT_BY = stringPreferencesKey("datastore_sort_by")
        val DATASTORE_COPY_OVERWRITE = booleanPreferencesKey("datastore_copy_overwrite")
        val DATASTORE_USER_ID = stringPreferencesKey("datastore_user_id")
        val DATASTORE_USER_PASSWORD = stringPreferencesKey("datastore_user_password")
        val DATASTORE_SERVER_IP = stringPreferencesKey("datastore_server_ip")
    }

    private val defaultPassword : String = ""
    private val defaultId : String = ""
    private val defaultIp : String = ""

    suspend fun saveSortBy(value:String) = DATASTORE_SORT_BY.save(value)
    suspend fun sortBy() = DATASTORE_SORT_BY.value()
    val sortBy = DATASTORE_SORT_BY.flow()

    suspend fun saveUserId(id:String) = DATASTORE_USER_ID.save(id)
    suspend fun getUserId() = DATASTORE_USER_ID.notNullValue(defaultValue = defaultId)
    val userIdFlow = DATASTORE_USER_ID.notNullFlow(defaultValue = defaultId)

    suspend fun saveUserPassword(password:String) = DATASTORE_USER_PASSWORD.save(password)
    suspend fun getUserPassword() = DATASTORE_USER_PASSWORD.notNullValue(defaultValue = defaultPassword)
    val userPasswordFlow = DATASTORE_USER_PASSWORD.notNullFlow(defaultValue = defaultPassword)

    suspend fun saveServerIp(ip:String) = DATASTORE_SERVER_IP.save(ip)
    suspend fun getServerIp() = DATASTORE_SERVER_IP.notNullValue(defaultValue = defaultIp)
    val serverIpFlow = DATASTORE_SERVER_IP.notNullFlow(defaultValue = defaultIp)

}