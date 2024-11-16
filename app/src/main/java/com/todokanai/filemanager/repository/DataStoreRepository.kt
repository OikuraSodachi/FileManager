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
    }

    suspend fun saveSortBy(value:String) = DATASTORE_SORT_BY.save(value)

    suspend fun sortBy() = DATASTORE_SORT_BY.value()

    val sortBy = DATASTORE_SORT_BY.flow()

    /*
    val sortBy_asString = dataStore.sortBy.map {
        when(it){
            1 -> {
                "BY_DEFAULT"
            }
            2->{
                "BY_NAME_ASCENDING"
            }
            3->{
                "BY_NAME_DESCENDING"
            }
            4->{
                "BY_SIZE_ASCENDING "
            }
            5->{
                "BY_SIZE_DESCENDING"
            }
            6->{
                "BY_TYPE_ASCENDING"
            }
            7->{
                "BY_TYPE_DESCENDING"
            }
            8->{
                "BY_DATE_ASCENDING"
            }
            9->{
                "BY_DATE_DESCENDING"
            }
            else->{
                "SortType value error"
            }
        }
    }
     */

    suspend fun saveCopyOverwrite(value:Boolean) = DATASTORE_COPY_OVERWRITE.save(value)

    suspend fun copyOverwrite() = DATASTORE_COPY_OVERWRITE.value()
}