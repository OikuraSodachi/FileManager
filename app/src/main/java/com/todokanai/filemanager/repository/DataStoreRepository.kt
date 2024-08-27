package com.todokanai.filemanager.repository

import com.todokanai.filemanager.data.datastore.MyDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(private val dataStore: MyDataStore){

    fun saveSortBy(value:String){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.saveSortBy(value)
        }
    }

    suspend fun sortBy() = dataStore.sortBy()

    val sortBy = dataStore.sortBy

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

    fun saveCopyOverwrite(value:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.saveCopyOverwrite(value)
        }
    }

    suspend fun copyOverwrite() = dataStore.copyOverwrite()



}