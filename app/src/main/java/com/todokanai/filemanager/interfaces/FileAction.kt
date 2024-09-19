package com.todokanai.filemanager.interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface FileAction {

    /** this is Not intended to be overridden manually
     *
     *  call this method to execute the action
     *
     *  calls onComplete() when finished
     * **/
    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            main()
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
}