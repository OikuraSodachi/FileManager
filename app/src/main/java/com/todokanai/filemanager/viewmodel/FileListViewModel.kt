package com.todokanai.filemanager.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.tools.actions.CopyAction
import com.todokanai.filemanager.tools.actions.DeleteAction
import com.todokanai.filemanager.tools.actions.MoveAction
import com.todokanai.filemanager.tools.actions.UnzipAction
import com.todokanai.filemanager.tools.actions.ZipAction
import com.todokanai.filemanager.tools.independent.FileModule
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import com.todokanai.filemanager.tools.independent.sortedFileList_td
import com.todokanai.filemanager.tools.independent.uploadFileToFtp_td
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/** modeManager를 viewModel에서 완전히 제거해야 함 **/
@HiltViewModel
class FileListViewModel @Inject constructor(private val dsRepo:DataStoreRepository,val module:FileModule):ViewModel(){

    val notAccessible =  module.notAccessible
    val directoryList = module.dirTree

    val fileHolderList = combine(
        module.listFiles,
        dsRepo.sortBy
    ){
        listFiles,mode ->
        sortedFileList_td(listFiles,mode).map{
            it.toFileHolderItem()
        }
    }

    /** "Hot Flow" of [fileHolderList] **/
    val fileHolderListTest = combine(
        module.listFiles,
        dsRepo.sortBy
    ){ listFiles,mode ->
       // println("listFiles: ${listFiles.map { it.name }}")
        //println("mode: $mode")
        sortedFileList_td(listFiles,mode)
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly // SharingStarted.Eagerly / Lazily to make the flow hot
    )

    fun refreshFileList() = module.refreshListFiles()

    fun onDirectoryClick(file:File) = module.updateCurrentPath(file.absolutePath)

    //fun onFileClick(context: Context, file: File) = module.onFileClick(context,file)
    fun onFileClick(context: Context, item:FileHolderItem) = module.onFileClick(context,item)

    fun onBackPressed() = module.onBackPressedCallback()

    fun copyWork(selected: Array<File>,targetDirectory: File){
        val action = CopyAction(selected,targetDirectory)
        action.start()
    }

    fun moveWork(selected: Array<File>,targetDirectory: File){
        MoveAction(selected,targetDirectory).start()
    }

    fun deleteWork(selected: Array<File>){
        DeleteAction(selected).start()
    }

    fun unzipWork(selected: File,targetDirectory: File){
        UnzipAction(selected,targetDirectory).start()
    }

    fun zipWork(selected:Array<File>,targetDirectory: File){
        ZipAction(selected, targetDirectory).start()
    }

    fun uploadToNas(selected: File){
        CoroutineScope(Dispatchers.IO).launch{
            uploadFileToFtp_td(
                server = "",
                username = "",
                password = "",
                localFilePath = selected.absolutePath,
                remoteFilePath = ""
            )
        }
    }

    private fun File.toFileHolderItem():FileHolderItem{
        val sizeText: String =
            if(this.isDirectory){
                "${this.listFiles()?.size} 개"
            }else{
                readableFileSize_td(this.length())
            }

        return FileHolderItem(
            absolutePath = this.absolutePath,
            size = sizeText,
            lastModified = this.lastModified(),
            uri = this.toUri()
        )
    }

}