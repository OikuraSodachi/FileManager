package com.todokanai.filemanager.interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

interface FileActionRecursive {

    /** this is Not intended to be overridden manually
     *
     *  call this method to execute the action
     *
     *  calls onComplete() when finished
     * **/
    fun start(selectedFiles:Array<File>) {
        CoroutineScope(Dispatchers.IO).launch {
            selectedFiles.forEach { file ->
                main(file)
            }
        }.invokeOnCompletion {
            onComplete()
        }
    }

    /** main part of the action (recursive part included inside) **/
    fun main(file: File):Unit{
        if(file.isDirectory){
            val files = file.listFiles()
            if (files != null) {
                for (child in files) {
                    main(child) // 재귀 호출
                }
            }
        }
        defaultAction(file)
    }

    /** cancel the action **/
    fun abort()

    /** for notification on progress **/
    fun progressCallback()

    /** callback when action is completed **/
    fun onComplete()


    fun callback()

    fun defaultAction(file: File)

}