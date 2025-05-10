package com.todokanai.filemanager.data.dataclass

import org.apache.commons.net.ftp.FTPFile
import java.io.File

/** FileTree 를 나타내는 View 를 위한 data class. data layer 에서 사용하지 말 것. **/
data class DirectoryHolderItem(
    val name: String,
    val absolutePath: String
) {

    companion object {
        fun fromFile(file: File): DirectoryHolderItem {
            return DirectoryHolderItem(
                name = file.name,
                absolutePath = file.absolutePath
            )
        }

        fun fromFTPFile(ftpFile: FTPFile, directory: String): DirectoryHolderItem {
            return DirectoryHolderItem(
                name = ftpFile.name,
                absolutePath = "$directory/${ftpFile.name}"
            )
        }

        fun fromAbsolutePath(absolutePath: String): DirectoryHolderItem {
            return DirectoryHolderItem(
                name = getLastSegment(absolutePath),
                absolutePath = absolutePath
            )
        }

        private fun getLastSegment(path: String): String {
            val regex = """[^/]*$""".toRegex()
            val matchResult = regex.find(path)
            return matchResult?.value ?: ""
        }
    }
}
