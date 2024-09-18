package com.todokanai.filemanager.interfaces

interface FileAction {

    fun start(){
        main()
        onComplete()
    }

    fun main()

    fun abort()

    fun progressCallback(processedBytes:Long)

    fun onComplete()
}