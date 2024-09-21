package com.todokanai.filemanager.interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

interface FileAction {

    /** call this method to execute the action
     *
     *  calls onComplete() when finished
     * **/
    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            main()
            delay(5000)
        }.invokeOnCompletion {
            onComplete()
        }
    }

    /** main part of the action **/
    fun main()

    /** cancel the action **/
    fun abort()

    /** for notification on progress **/
    fun progressCallback()

    /** callback when action is completed **/
    fun onComplete()


    /*
    /** call this method to execute the action
     *
     *  calls onComplete() when finished
     * **/
    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            mainGetter()
        }.invokeOnCompletion {
            onComplete()
        }
    }

    /** **/
    fun mainGetter()

    /** main part of the action (recursive part included inside) **/
    fun main(files:Array<File>){
        files.forEach { file ->
            if (file.isDirectory) {
                val listFiles = file.listFiles()
                if (listFiles != null) {
                    main(listFiles)
                }
            }
            defaultAction(file)
        }
    }

    /** cancel the action **/
    fun abort()

    /** for notification on progress **/
    fun progressCallback()

    /** callback when action is completed **/
    fun onComplete()


    fun callback()

    fun defaultAction(file: File)

     */
}