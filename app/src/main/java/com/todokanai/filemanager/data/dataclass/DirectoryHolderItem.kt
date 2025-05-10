package com.todokanai.filemanager.data.dataclass

import org.apache.commons.net.ftp.FTPFile
import java.io.File

data class DirectoryHolderItem(
    val name: String,
    /** 대응하는 File 을 특정할 수 있어야 함 **/
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
