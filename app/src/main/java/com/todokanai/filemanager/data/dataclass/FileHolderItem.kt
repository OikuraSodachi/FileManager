package com.todokanai.filemanager.data.dataclass

import android.net.Uri
import com.todokanai.filemanager.tools.independent.getMimeType_td
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

/** 주의: data layer 에서 이 class 를 사용하지 말 것
 *
 * @param uri File.toUri() **/
data class FileHolderItem(
    val absolutePath: String,
    val size: String,
    val lastModified: Long,
    val uri: Uri,
    var isSelected: Boolean = false
) {

    private fun mimeType() = getMimeType_td(absolutePath)

    /** File.isDirectory **/
    fun isDirectory(): Boolean {
        if(Files.isDirectory(Path(absolutePath))){
            return true
        } else {
            return false
        }
    }

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

    /** File.name **/
    fun name() = """[^/\\]+$""".toRegex().find(absolutePath)?.value

    /** @return the original file **/
    fun file(): File = File(absolutePath)
}