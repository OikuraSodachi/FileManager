package com.todokanai.filemanager.repository

import com.todokanai.filemanager.tools.NetFileModule
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NetUiRepository @Inject constructor(
    netModule: NetFileModule,
    serverRepo: ServerInfoRepository
) {

    val loggedIn = netModule.loggedIn

    val dirTreeNew = netModule.currentDirectory.map {
        absolutePathTree(it)
    }

    val serverListFlow = serverRepo.serverInfoFlow

    val itemList = netModule.itemList

    /** absolutePath 의 tree **/
    private fun absolutePathTree(absolutePath:String):List<String>{
        val result = mutableListOf<String>()
        var target = absolutePath
        while (target != "") {
            result.add(
                getLastSegment(target)
            )
            target = parentDirectory(target)
        }
        result.reverse()
        return result
    }

    private fun parentDirectory(path: String): String {
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