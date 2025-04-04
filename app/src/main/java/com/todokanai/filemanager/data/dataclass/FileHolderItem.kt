package com.todokanai.filemanager.data.dataclass

import android.net.Uri
import com.todokanai.filemanager.tools.independent.getMimeType_td

/** @param uri File.toUri() **/
data class FileHolderItem(
    val absolutePath:String,
    val size:String,
    val lastModified:Long,
    val uri:Uri,
    var isSelected:Boolean = false
){

    private fun mimeType() = getMimeType_td(absolutePath)

    /** File.isDirectory **/
    fun isDirectory():Boolean{
        if(getMimeType_td(absolutePath)== "resource/folder"){
            return true
        } else{
            return false
        }
    }

    fun isImage():Boolean{
        val temp = mimeType()
        if(temp=="video/*" || temp == "image/*" ){
            return true
        }else{
            return false
        }
    }

    /** File.extension **/
    fun extension() = """\.[^.]+$""".toRegex().find(absolutePath)?.value

    /** File.name **/
    fun name() = """[^/\\]+$""".toRegex().find(absolutePath)?.value
}