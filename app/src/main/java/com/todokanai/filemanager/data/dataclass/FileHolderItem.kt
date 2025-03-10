package com.todokanai.filemanager.data.dataclass

import android.net.Uri
import com.todokanai.filemanager.tools.independent.getMimeType_td
import java.text.DateFormat

/** @param uri File.toUri() **/
data class FileHolderItem(
    val absolutePath:String,
    val size:Long,
    val lastModified:Long,
    val uri:Uri
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

    fun sizeText():String{
        if(isDirectory()){
            return "${contentNumber()} files"
        }else{
            return DateFormat.getDateTimeInstance().format(lastModified)
        }
    }

    /** TODO() number of files inside the directory (if it is Directory)
     * File 이 Local 에 있는 경우에만 사용해야 할 듯 **/
    fun contentNumber():Int{
        return 0
    }
}