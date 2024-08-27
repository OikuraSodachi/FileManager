package com.todokanai.filemanager.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.todokanai.filemanager.myobjects.Constants.BY_DEFAULT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MyDataStore(private val appContext:Context) {
    companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mydatastore")
        val DATASTORE_SORT_BY = stringPreferencesKey("datastore_sort_by")
        val DATASTORE_COPY_OVERWRITE = booleanPreferencesKey("datastore_copy_overwrite")
    }

    suspend fun saveSortBy(value:String){
        appContext.dataStore.edit{
            it[DATASTORE_SORT_BY] = value
        }
    }

    suspend fun sortBy() : String {
        return appContext.dataStore.data.first()[DATASTORE_SORT_BY] ?:BY_DEFAULT
    }

    val sortBy: Flow<String> = appContext.dataStore.data.map{
        it[DATASTORE_SORT_BY] ?: BY_DEFAULT
    }

    suspend fun saveCopyOverwrite(value:Boolean){
        appContext.dataStore.edit{
            it[DATASTORE_COPY_OVERWRITE] = value
        }
    }

    suspend fun copyOverwrite(): Boolean {
        return appContext.dataStore.data.first()[DATASTORE_COPY_OVERWRITE] ?: false
    }
}