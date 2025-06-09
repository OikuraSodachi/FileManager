package com.todokanai.filemanager.tools

import com.todokanai.filemanager.abstracts.FileModuleLogics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject

class NetFileModule @Inject constructor(
    val coroutineDispatcher: CoroutineDispatcher,
    val ftpClient: FTPClient,
    defaultPath: String
) : FileModuleLogics<FTPFile>(defaultPath) {

    val itemList = currentDirectory.map { directory ->
        ftpClient.listFiles(directory).map {
            Pair(it, directory)
        }.toTypedArray()
    }.flowOn(coroutineDispatcher)

    suspend fun logout() = withContext(coroutineDispatcher) {
        ftpClient.run {
            logout()
            disconnect()
        }       // Todo: 아직 미검증 상태
    }

    override suspend fun isDirectoryValid(directory: String): Boolean =
        withContext(coroutineDispatcher) {
            try {
                ftpClient.mlistFile(directory)
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
}