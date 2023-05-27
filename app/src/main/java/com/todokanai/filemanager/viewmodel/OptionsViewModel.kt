package com.todokanai.filemanager.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.tools.FileAction
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(): ViewModel(){

    private val fAction = FileAction()
    val path = fAction.currentPath
    val storageList = fAction.storageList

    fun dirTree(file: File) = fAction.fileTree(file)

    private val listVal: List<String>
        get() = storageList.value

    fun leftBtn(context: Context,anchor: View){
        val internalPath = Environment.getExternalStorageDirectory().path
        val popupMenu = PopupMenu(context,anchor)
        popupMenu.menu.add("Internal Storage")
        for(index in 0..listVal.size-1){
            popupMenu.menu.add(listVal[index])
            popupMenu.setOnMenuItemClickListener {
                if(it.title == "Internal Storage"){
                    path.value = internalPath
                } else if(it.title == listVal[index]){
                    path.value = listVal[index]
                }
                true
            }
        }
        popupMenu.show()
    }

    fun middleBtn(activity: Activity){


        val text = Environment.getRootDirectory()
        val text2 = Environment.getDataDirectory()
        val text3 = Environment.getStorageDirectory()

        Toast.makeText(activity,"$text\n$text2\n$text3",Toast.LENGTH_SHORT).show()



    }

    fun rightBtn(context: Context, anchor: View){
        val popupMenu = PopupMenu(context,anchor)
        popupMenu.menu.add("Create New Folder")
        popupMenu.setOnMenuItemClickListener {
            if(it.title == "Create New Folder"){
                fAction.createFolderAction(context,path.value)
            }
            true
        }
        popupMenu.show()
    }

    //---------------

    fun onItemClick(context: Context,directory: File){
        fAction.openAction(context,directory)
    }

    fun updateContents() = fAction.updateContents()

    fun updateStorageList(storages:List<String>) = fAction.updateStorageList(storages)


}