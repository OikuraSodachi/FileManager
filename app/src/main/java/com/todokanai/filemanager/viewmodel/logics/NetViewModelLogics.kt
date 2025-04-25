package com.todokanai.filemanager.viewmodel.logics

import androidx.lifecycle.ViewModel
import com.todokanai.filemanager.data.dataclass.DirectoryHolderItem
import com.todokanai.filemanager.data.dataclass.FileHolderItem
import kotlinx.coroutines.flow.Flow

abstract class NetViewModelLogics : ViewModel() {

    protected abstract val dirTree: Flow<List<DirectoryHolderItem>>
    protected abstract val itemList: Flow<List<FileHolderItem>>

    abstract fun login()
    abstract fun onItemClick(item: FileHolderItem)
    abstract fun onDirectoryClick(item: DirectoryHolderItem)
    abstract fun toParent()

    protected fun convertToDirTree(absolutePath: String): List<DirectoryHolderItem> {
        val result = mutableListOf<DirectoryHolderItem>()
        var target = absolutePath
        while (target != "") {
            result.add(
                DirectoryHolderItem(
                    name = getLastSegment(target),
                    absolutePath = target
                )
            )
            target = testRegex(target)
        }
        result.reverse()
        return result
    }

    private fun testRegex(path: String): String {
        val regex = """(.*)/[^/]*$""".toRegex()
        val matchResult = regex.find(path)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun getLastSegment(path: String): String {
        val regex = """[^/]*$""".toRegex()
        val matchResult = regex.find(path)
        return matchResult?.value ?: ""
    }

}