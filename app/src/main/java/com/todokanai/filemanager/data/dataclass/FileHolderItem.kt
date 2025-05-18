package com.todokanai.filemanager.data.dataclass

import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import org.apache.commons.net.ftp.FTPFile
import java.io.File

/** File Item 을 나타내는 View 를 위한 data class. data layer 에서 사용하지 말 것. **/
data class FileHolderItem(
    val absolutePath: String,
    val name: String,
    val size: String,
    val lastModified: Long,
    val isDirectory: Boolean,
    var isSelected: Boolean = false
) {

    fun isImage(): Boolean {
        val type = getMimeType_td(absolutePath)
        if (type == "video/*" || type == "image/*") {
            return true
        } else {
            return false
        }
    }

    /** File.extension **/
    fun extension() = """\.[^.]+$""".toRegex().find(absolutePath)?.value

    companion object {

        fun fromFile(file: File): FileHolderItem {

            val sizeText =
                if (file.isDirectory) {
                    "${file.listFiles()?.size} 개"
                } else {
                    readableFileSize_td(file.length())
                }
            return FileHolderItem(
                absolutePath = file.absolutePath,
                name = file.name,
                size = sizeText,
                lastModified = file.lastModified(),
                isDirectory = file.isDirectory
            )
        }

        fun fromFTPFile(ftpFile: FTPFile, currentDirectory: String): FileHolderItem {
            val absolutePathTemp = "${currentDirectory}/${ftpFile.name}"
            val sizeText =
                if (ftpFile.isDirectory) {
                    ""
                } else {
                    readableFileSize_td(ftpFile.size)
                }
            val lastModified = ftpFile.timestamp.timeInMillis // Todo: 값이 의도와는 다르게 찍히고 있음. 내부 로직이 File.lastModified() 와 조금 다른 듯?
            return FileHolderItem(
                absolutePath = absolutePathTemp,
                name = ftpFile.name,
                size = sizeText,
                lastModified = lastModified,
                isDirectory = ftpFile.isDirectory
            )
        }
    }

}