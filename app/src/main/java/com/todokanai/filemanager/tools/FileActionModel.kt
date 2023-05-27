package com.todokanai.filemanager.tools

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.content.FileProvider
import com.github.junrar.Archive
import com.github.junrar.exception.RarException
import com.github.junrar.rarfile.FileHeader
import kotlinx.coroutines.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.math.log10
import kotlin.math.pow

/**
 *  이 클래스의 method는 독립적으로 사용 가능함
 *
 */

class FileActionModel {

    fun openFile(context: Context, selectedFile: File,authority:String){
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = FLAG_GRANT_READ_URI_PERMISSION
            intent.setDataAndType(FileProvider.getUriForFile(context,
                "$authority.provider",
                selectedFile
            ), getMimeType(selectedFile.name))
            startActivity(context,intent,null)
        } catch (e: Exception) {

            Toast.makeText(
                context.applicationContext,
                "Cannot open the file",
                Toast.LENGTH_SHORT
            ).show()
            println(e)
        }
    }

    suspend fun deleteFile(files: List<File>, myCallback: (fileName:String, bytesDeleted: Long) -> Unit, totalDeleted: Long = 0L) :Unit = withContext(Dispatchers.IO) {
        var bytesDeleted = totalDeleted
        fun getTotalSizeD(files: List<File>): Long {
            var totalSize: Long = 0
            for (file in files) {
                if (file.isFile) {
                    totalSize += file.length()
                } else if (file.isDirectory) {
                    totalSize += getTotalSizeD(file.listFiles()?.toList() ?: emptyList())
                }
            }
            return totalSize
        }
        files.forEach { file ->
            if (file.isDirectory) {
                val subFiles = file.listFiles().toList()
                bytesDeleted += getTotalSizeD(subFiles)
            } else {
                bytesDeleted += file.length()
            }
        }

        var deletedSize = 0L
        var lastCallbackTime = 0L
        files.forEach { file ->
            if (file.isDirectory) {
                val subFiles = file.listFiles()
                if (subFiles != null) {
                    deleteFile(subFiles.toList(), myCallback)
                }
            }
            val fileSize = file.length()
            file.delete()
            deletedSize += fileSize

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastCallbackTime >= 250L) { // 0.25초마다 myCallback 호출
                myCallback(file.name, bytesDeleted)
                lastCallbackTime = currentTime
            }
        }
    }

    suspend fun deleteWithoutCallback(files: List<File>) :Unit = withContext(Dispatchers.IO) {
        files.forEach { file ->
            file.deleteRecursively()
        }
    }

    suspend fun deleteSingleFile(file: File) :Unit = withContext(Dispatchers.IO) {
        file.deleteRecursively()
    }

    suspend fun copyFile(files: List<File>, path: String, myCallback: (fileName: String, bytesCopied: Long) -> Unit, totalCopied: Long = 0L): Unit = withContext(Dispatchers.IO) {
        var bytesCopied = totalCopied
        fun getTotalSizeC(files: List<File>): Long {
            var totalSize: Long = 0
            for (file in files) {
                if (file.isFile) {
                    totalSize += file.length()
                } else if (file.isDirectory) {
                    totalSize += getTotalSizeC(file.listFiles()?.toList() ?: emptyList())
                }
            }
            return totalSize
        }

        files.forEach { file ->
            val destination = File("$path/${file.name}")
            var newFileName = destination
            if (destination.exists()) {
                // 대충 코루틴 전체 중단하고 Dialog를 통해서 값 받아올것
                // 아무것도 안하면 덮어쓰기 적용됨

                //----------
                val newName = "$path/${file.nameWithoutExtension + "(1)"}${file.extension}"
                newFileName = File(newName)   // 새로운 이름(newName)으로 저장 예시
                //----------
            }

            if (file.isDirectory) {
                newFileName.mkdirs()
                val subFiles = file.listFiles()?.toList() ?: emptyList()
                copyFile(subFiles, newFileName.absolutePath, myCallback, bytesCopied)
                bytesCopied += getTotalSizeC(subFiles)
            } else {
                val buffer = ByteArray(8192)
                file.inputStream().use { inputStream ->
                    newFileName.outputStream().use { outputStream ->
                        var bytes = inputStream.read(buffer)        // 90162
                        var lastCallbackTime = System.currentTimeMillis()   //448115
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            bytesCopied += bytes
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastCallbackTime >= 250) {
                                myCallback(file.name, bytesCopied)
                                lastCallbackTime = currentTime
                            }
                            bytes = inputStream.read(buffer)
                        }
                    }
                }
            }
        }
    }





    fun createFolder(path:String,name:String){
        try {
            File(path, name).mkdir()
        } catch(e:Throwable){
            println(e)
        }
    }

    fun renameFileOrFolder(selectedFile: File, name:String) = selectedFile.renameTo(File("${selectedFile.parent}/$name"))

    fun fileTree(file:File):List<File>{
        val result = mutableListOf<File>()
        var temp : File = file
        while(temp.parentFile != null){
            result.add(temp)
            temp = temp.parentFile
        }
        return result.asReversed()
    }


    //----------------------
    /** Function to get the character sequence from after the last instance of File.separatorChar in a path
     * @author Neeyat Lotlikar
     * @param path String path representing the file
     * @return String filename which is the character sequence from after the last instance of File.separatorChar in a path
     * if the path contains the File.separatorChar. Else, the same path.*/
    fun getFilenameForPath(path: String): String =
        if (!path.contains(File.separatorChar)) path
        else path.subSequence(
            path.lastIndexOf(File.separatorChar) + 1, // Discard the File.separatorChar
            path.length // parameter is used exclusively. Substring produced till n - 1 characters are reached.
        ).toString()

    /** Function to get the MimeType from a filename by comparing it's file extension
     * @author Neeyat Lotlikar
     * @param filename String name of the file. Can also be a path.
     * @return String MimeType */
    fun getMimeType(filename: String): String = if (filename.lastIndexOf('.') == -1)
        "resource/folder"
    else
        when (filename.subSequence(
            filename.lastIndexOf('.'),
            filename.length
        ).toString().toLowerCase(Locale.ROOT)) {
            ".doc", ".docx" -> "application/msword"
            ".pdf" -> "application/pdf"
            ".ppt", ".pptx" -> "application/vnd.ms-powerpoint"
            ".xls", ".xlsx" -> "application/vnd.ms-excel"
            ".zip", ".rar" -> "application/x-wav"
            ".7z" -> "application/x-7z-compressed"
            ".rtf" -> "application/rtf"
            ".wav", ".mp3", ".m4a", ".ogg", ".oga", ".weba" -> "audio/*"
            ".ogx" -> "application/ogg"
            ".gif" -> "image/gif"
            ".jpg", ".jpeg", ".png", ".bmp" -> "image/*"
            ".csv" -> "text/csv"
            ".m3u8" -> "application/vnd.apple.mpegurl"
            ".txt", ".mht", ".mhtml", ".html" -> "text/plain"
            ".3gp", ".mpg", ".mpeg", ".mpe", ".mp4", ".avi", ".ogv", ".webm" -> "video/*"
            else -> "*/*"
        }
    //------------------

    fun readableFileSize(size: Long): String? {
        if (size <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    suspend fun compressToZip(files: List<File>, zipFile: File) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            val out = ZipOutputStream(FileOutputStream(zipFile))

            files.forEach { file ->
                val path = if (file.isDirectory) "${file.name}/" else file.name
                val entry = ZipEntry(path)
                out.putNextEntry(entry)

                if (!file.isDirectory) {
                    val inputStream = FileInputStream(file)
                    var len: Int
                    while (inputStream.read(buffer).also { len = it } > 0) {
                        out.write(buffer, 0, len)
                    }
                    inputStream.close()
                }
                out.closeEntry()
            }
            out.close()
        }
    }

    suspend fun unZip(zipFile: File, targetPath: String) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            val zipInputStream = ZipInputStream(zipFile.inputStream())
            var zipEntry: ZipEntry? = zipInputStream.nextEntry

            while (zipEntry != null) {
                val newFile = File(targetPath, zipEntry.name)
                if (zipEntry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.mkdirs()
                    val fileOutputStream = newFile.outputStream()
                    var length: Int
                    while (zipInputStream.read(buffer).also { length = it } > 0) {
                        fileOutputStream.write(buffer, 0, length)
                    }
                    fileOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.closeEntry()
            zipInputStream.close()
        }
    }

    fun physicalStorage(file: File): String {
        val path = file.absolutePath
        val firstSlashIndex = path.indexOf('/')
        val secondSlashIndex = path.indexOf('/', startIndex = firstSlashIndex + 1)
        val thirdSlashIndex = path.indexOf('/', startIndex = secondSlashIndex + 1)
        return if (thirdSlashIndex > secondSlashIndex) {
            path.substring(secondSlashIndex + 1, thirdSlashIndex)
        } else {
            path.substring(secondSlashIndex + 1)
        }
    }       // 안드로이드에서만 적용 가능?

    fun getTotalSize(files: List<File>): Long {
        var totalSize: Long = 0
        for (file in files) {
            if (file.isFile) {
                totalSize += file.length()
            } else if (file.isDirectory) {
                totalSize += getTotalSize(file.listFiles()?.toList() ?: emptyList())
            }
        }
        return totalSize
    }



    suspend fun unRar1(file: File, targetPath: String) {
        withContext(Dispatchers.IO) {
            try {
                val archive = Archive(file.absoluteFile)
                var fileHeader = archive.nextFileHeader()
                while (fileHeader != null) {
                    if (!fileHeader.isDirectory) {
                        val fileName = fileHeader.fileNameString
                        val outFile = File("/storage/emulated/0/Documents/$fileName")
                        val outputStream = FileOutputStream(outFile)
                        archive.extractFile(fileHeader, outputStream)
                        outputStream.close()
                    }
                    fileHeader = archive.nextFileHeader()
                }
            }catch (e:Throwable){
                println(e)
            }
        }
    }

    suspend fun unRar(file: File,path: String) {
        withContext(Dispatchers.IO) {

            try {
                val archive = Archive(file)
                var header: FileHeader? = archive.nextFileHeader()
                while (header != null) {
                    val outputPath = File(file.parentFile, header.fileNameString.trim())
                    if (header.isDirectory) {
                        // 폴더를 생성합니다.
                        outputPath.mkdirs()
                    } else {
                        // 파일을 생성합니다.
                        val outputStream = FileOutputStream(outputPath)
                        archive.extractFile(header, outputStream)
                        outputStream.close()
                    }
                    header = archive.nextFileHeader()
                }
                archive.close()
            } catch (e: RarException) {
                e.printStackTrace()
                println(e)
            }
        }
    }

    suspend fun getThumbnail(file:File): Bitmap = withContext(Dispatchers.IO){ ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile(file.absolutePath), 180, 180)}


}