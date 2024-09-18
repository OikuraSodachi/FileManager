package com.todokanai.filemanager.interfaces

interface FileAction {
    fun start()

    fun abort()

    fun progressCallback(processedBytes:Long)

    fun onComplete()
}