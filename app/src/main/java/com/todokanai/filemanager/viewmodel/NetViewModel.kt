package com.todokanai.filemanager.viewmodel

import android.net.Uri
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.tools.NetFileModule
import com.todokanai.filemanager.viewmodel.basemodel.BaseNetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject

@HiltViewModel
class NetViewModel @Inject constructor(val module:NetFileModule) : BaseNetViewModel(module){

    private val testUri: Uri = Uri.EMPTY

    override fun toFileHolderItem(ftpFile: FTPFile, currentDirectory: String): FileHolderItem {
        return FileHolderItem(
            absolutePath = "${currentDirectory}/${ftpFile.name}",
            size = ftpFile.size,
            lastModified = ftpFile.timestamp.timeInMillis,
            uri = testUri
        )
    }

    override fun onItemClick(item: FileHolderItem){
        if(item.isDirectory()) {
            module.setCurrentDirectory(item.absolutePath)
        }else{
            println("this is a File")
        }
    }
}