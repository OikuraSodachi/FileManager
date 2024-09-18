package com.todokanai.filemanager.tools.actions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class CopyAction {
    var progress : Int = 0



    /** independent **/
    suspend fun copyFiles_Recursive_td(
        selected:Array<File>,
        targetDirectory: File,
        onProgress:(File)->Unit,
        copyOption: CopyOption = StandardCopyOption.REPLACE_EXISTING
    ):Unit = withContext(Dispatchers.IO){
        for (file in selected) {
            val target = targetDirectory.resolve(file.name)
            if (file.isDirectory) {
                // Create the target directory
                target.mkdirs()
                onProgress(file)
                // Copy the contents of the directory recursively
                copyFiles_Recursive_td(file.listFiles() ?: arrayOf(), target,onProgress, copyOption)
            } else {
                // Copy the file
                Files.copy(file.toPath(), target.toPath(),)
                onProgress(file)
            }
        }
    }

}