package com.todokanai.filemanager.interfaces

interface FileAction {

    /** this is Not intended to be accessed manually **/
    fun start(){
        main()
        onComplete()
    }

    /** main part of the action. will include onComplete() at the end **/
    fun main()

    /** cancel the action **/
    fun abort()

    /** for notification on progress **/
    fun progressCallback(processedBytes:Long)

    /** callback when action is completed **/
    fun onComplete()
}