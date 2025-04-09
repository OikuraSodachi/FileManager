package com.todokanai.filemanager.viewmodel.basemodel

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.FileHolderItem

abstract class BaseNetViewModel() : ViewModel() {

    abstract fun onItemClick(item: FileHolderItem)

    abstract fun setCurrentDirectory(absolutePath:String)

    abstract fun toParent()

}
