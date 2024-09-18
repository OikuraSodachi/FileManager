package com.todokanai.filemanager.interfaces

import java.io.File

interface FileActionRecursive {

    /** when file.isDirectory == false **/
    fun mainAction(file: File){

    }

    fun callback()

    fun wrapper_internal(
        file: File,
        temp_callback:()->Unit
    ){
        if(file.isDirectory){
            val files = file.listFiles()
            if (files != null) {
                for (child in files) {
                    wrapper_internal(child,temp_callback) // 재귀 호출
                }
            }
        }
        mainAction(file)
    }

}