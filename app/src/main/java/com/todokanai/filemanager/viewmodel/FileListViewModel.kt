package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.myobjects.Constants.ACTION_COPY
import com.todokanai.filemanager.myobjects.Constants.ACTION_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.myobjects.MyObjects
import com.todokanai.filemanager.tools.FileAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okio.Path.Companion.toPath
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class FileListViewModel @Inject constructor(): ViewModel(){

    var selectedItem : File? = null
    val selectedList = MutableStateFlow<MutableSet<File>>(mutableSetOf<File>())
    private val fAction = FileAction()
    val selectMode = MyObjects.selectMode
    val fileList = MyObjects.fileList
    val path = MyObjects.currentPath

    lateinit var subjectList : Set<File>
    var confirmActionType : Int = 0

    fun updateContents() = fAction.updateContents()

    fun onBackPressed(){
        when(selectMode.value){
            DEFAULT_MODE -> {
                toParentDirectory()
            }
            MULTI_SELECT_MODE -> {
                selectMode.value = DEFAULT_MODE
            }
            CONFIRM_MODE -> {
                toParentDirectory()
            }
        }
    }

    fun exit(activity: Activity){
        ActivityCompat.finishAffinity(activity)
        System.runFinalization()
        exitProcess(0)
    }

    fun onItemClick(context: Context,selectedFile: File){
        selectedItem = selectedFile
        when(selectMode.value){
            DEFAULT_MODE -> {
                fAction.openAction(context, selectedFile)
            }
            MULTI_SELECT_MODE -> {
                selectedItem = selectedFile
                fAction.updateSelectedList(selectedList.value,selectedFile)
            }
            CONFIRM_MODE -> {
                if(selectedFile.isDirectory) {
                    fAction.openAction(context, selectedFile)
                }
            }
        }
    }

    fun onItemLongClick(selectedFile: File){
        selectedItem = selectedFile
        when(selectMode.value){
            DEFAULT_MODE -> {
                selectMode.value = MULTI_SELECT_MODE
                selectedList.value.add(selectedFile)
            }
            MULTI_SELECT_MODE -> {

            }
            CONFIRM_MODE -> {

            }
        }
    }

    fun moveBtn(selectedList: Set<File>){
        subjectList = selectedList
        confirmActionType = ACTION_MOVE
        selectMode.value = CONFIRM_MODE
    }

    fun copyBtn(selectedList: Set<File>){
        subjectList = selectedList
        confirmActionType = ACTION_COPY
        selectMode.value = CONFIRM_MODE
    }

    fun renameBtn(context: Context) = fAction.renameAction(context, selectedItem!!)

    fun deleteBtn(context: Context) = fAction.deleteAction(context,selectedList.value)

    fun moreBtn(context: Context,anchor: View){
        val popupMenu = PopupMenu(context,anchor)
        popupMenu.menu.add("Create New Folder")
        popupMenu.menu.add("Zip")
        popupMenu.menu.add("Unzip")
        popupMenu.menu.add("Info")
        popupMenu.setOnMenuItemClickListener {
            if(it.title == "Create New Folder"){
                fAction.createFolderAction(context,path.value)
            } else if (it.title == "Zip"){
                fAction.zipAction(selectedList.value.toList(),File(path.value + "/files.zip"))
                selectMode.value = DEFAULT_MODE
            } else if (it.title == "Unzip"){
                fAction.unzipAction(selectedList.value.first(),path.value+"/${selectedList.value.first().nameWithoutExtension}")
                selectMode.value = DEFAULT_MODE
            } else if (it.title == "Info"){
                fAction.info(context, selectedList.value.toList())

            }
            true
        }
        popupMenu.show()
    }

    fun cancelBtn(){
        when(selectMode.value){
            DEFAULT_MODE -> {

            }
            MULTI_SELECT_MODE -> {

            }
            CONFIRM_MODE -> {
                selectMode.value = DEFAULT_MODE
            }
        }
    }

    fun confirmMoreBtn(){

    }

    fun confirmBtn(action:Int){
        when(action){
            ACTION_COPY -> {
                fAction.copyAction(subjectList.toList(), path.value)
            }
            ACTION_MOVE -> {
                fAction.moveAction(subjectList.toList(), path.value)
            }
        }
        selectMode.value = DEFAULT_MODE
    }
    
    fun resetSelectedList(){
        selectedList.value = mutableSetOf<File>()
    }

    private fun toParentDirectory(){
        path.value = path.value.toPath().parent.toString()
    }

}