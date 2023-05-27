package com.todokanai.filemanager.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.todokanai.filemanager.application.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreRepository {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mydatastore")
        val DATASTORE_STRING = stringPreferencesKey("datastore_string")
        val DATASTORE_OVERWRITE = booleanPreferencesKey("datastore_overwrite")
    }
    private val myContext = MyApplication.appContext

    fun saveString( value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            myContext.dataStore.edit {
                it[DATASTORE_STRING] = value
                Log.d("datastore", "datastore_string insert: $value")
            }
        }
    }

    val dataStoreString: Flow<String?> = myContext.dataStore.data.map{
        it[DATASTORE_STRING]
    }

    suspend fun dataStoreString() : String? {
        return myContext.dataStore.data.first()[DATASTORE_STRING]
    }
    //------------------

    fun saveOverwrite(value:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            myContext.dataStore.edit {
                it[DATASTORE_OVERWRITE] = value
            }
        }

    }

    val dataStoreOverwrite: Flow<Boolean> = myContext.dataStore.data.map{
        it[DATASTORE_OVERWRITE] ?:false
    }

    suspend fun dataStoreOverwrite() : Boolean {
        return myContext.dataStore.data.first()[DATASTORE_OVERWRITE] ?:false
    }
}