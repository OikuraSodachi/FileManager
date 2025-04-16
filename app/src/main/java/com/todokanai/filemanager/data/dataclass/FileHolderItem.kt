package com.todokanai.filemanager.data.dataclass

import android.graphics.Bitmap
import com.todokanai.filemanager.tools.independent.getMimeType_td
import com.todokanai.filemanager.tools.independent.readableFileSize_td
import org.apache.commons.net.ftp.FTPFile
import java.io.File

/** 주의: data layer 에서 이 class 를 사용하지 말 것 **/
data class FileHolderItem(
    val absolutePath: String,
    val name: String,
    val size: String,
    val lastModified: Long,
    val isDirectory: Boolean,
    val thumbnail: Bitmap? = null,
    var isSelected: Boolean = false
) {

    private fun mimeType() = getMimeType_td(absolutePath)

    fun isImage(): Boolean {
        val type = mimeType()
        if (type == "video/*" || type == "image/*") {
            return true
        } else {
            return false
        }
    }

    /** File.extension **/
    fun extension() = """\.[^.]+$""".toRegex().find(absolutePath)?.value

    /** @return the original file **/
    fun file(): File = File(absolutePath)

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
            return FileHolderItem(
                absolutePath = absolutePathTemp,
                name = ftpFile.name,
                size = sizeText,
                lastModified = ftpFile.timestamp.timeInMillis,
                isDirectory = ftpFile.isDirectory
            )
        }
    }

}