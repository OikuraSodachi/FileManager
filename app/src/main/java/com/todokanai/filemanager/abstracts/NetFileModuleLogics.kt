package com.todokanai.filemanager.abstracts

import org.apache.commons.net.ftp.FTPFile

abstract class NetFileModuleLogics {

    abstract suspend fun setCurrentDirectory(directory: String)

    /** 현재 directory 내부의 파일 목록 가져오기 **/
    abstract suspend fun ftpListFiles():Array<FTPFile>

}