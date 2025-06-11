package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.tools.independent.getFileAndFoldersNumber_td
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/** @param selectedFiles absolutePaths of selected files
 *  @param targetDirectory absolutePath of target directory **/
class CopyAction(
    val selectedFiles: Collection<String>,
    val targetDirectory: String
) {
    var progress: Int = 0
    private val selected = selectedFiles.map { File(it) }.toTypedArray()
    private val directory = File(targetDirectory)
    private val fileQuantity = getFileAndFoldersNumber_td(selected)
    private lateinit var currentFileInProcess: File

    fun main() {
        CoroutineScope(Dispatchers.IO).launch {
            copyFiles_Recursive_td(
                selected = selected,
                targetDirectory = directory,
                onProgress = {
                    progress++
//        myNoti.sendSilentNotification(
//            title = currentFileInProcess.name,
//            message = "$progress/$fileQuantity"
//        )
                }
            )
        }
    }


    fun copyFiles_Recursive_td(
        selected: Array<File>,
        targetDirectory: File,
        onProgress: (File) -> Unit
    ) {
        selected.forEach { file ->
            val target = targetDirectory.resolve(file.name)
            currentFileInProcess = target
            if (file.isDirectory) {
                target.mkdirs()
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target, onProgress)
            } else {
                Files.copy(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            onProgress(file)
        }
    }
}