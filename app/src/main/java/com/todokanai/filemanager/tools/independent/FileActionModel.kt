package com.todokanai.filemanager.tools.independent

/** 이 method들은 독립적으로 사용 가능함 */

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.log10
import kotlin.math.pow

//----------------------
/** Todokanai
 *
 * Function to get the character sequence from after the last instance of File.separatorChar in a path
 * @author Neeyat Lotlikar
 * @param path String path representing the file
 * @return String filename which is the character sequence from after the last instance of File.separatorChar in a path
 * if the path contains the File.separatorChar. Else, the same path.*/
fun getFilenameForPath_td(path: String): String =
    if (!path.contains(File.separatorChar)) path
    else path.subSequence(
        path.lastIndexOf(File.separatorChar) + 1, // Discard the File.separatorChar
        path.length // parameter is used exclusively. Substring produced till n - 1 characters are reached.
    ).toString()

/** Todokanai */
fun readableFileSize_td(size: Long): String {
    if (size <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(
        size / 1024.0.pow(digitGroups.toDouble())
    ) + " " + units[digitGroups]
}


/** Todokanai
 *
 * get the total size of [files]:Array<[File]> and its subdirectories
 * @param files Array of [File]
 * @return the total size
 * */
fun getTotalSize_td(files: Array<File>): Long {
    var totalSize: Long = 0
    for (file in files) {
        if (file.isDirectory) {
            totalSize += getTotalSize_td(file.listFiles() ?: emptyArray())
        } else {
            totalSize += file.length()
        }
    }
    return totalSize
}

/** Todokanai
 *
 * @return the number of [File] on [files] and its subdirectories. Does NOT Include directories
 * **/
fun getFileNumber_td(files: Array<File>): Int {
    var total = 0
    for (file in files) {
        if (file.isFile) {
            total++
        } else if (file.isDirectory) {
            total += getFileNumber_td(file.listFiles() ?: emptyArray())
        }
    }
    return total
}

/** Todokanai
 * get the total number of files on [files] and its subdirectories
 * @param files Array of [File]
 * @return the total number
 * Directory와 File의 총 갯수*/
fun getFileAndFoldersNumber_td(files: Array<File>): Int {
    var total = 0
    for (file in files) {
        if (file.isFile) {
            total++
        } else if (file.isDirectory) {
            total++
            total += getFileNumber_td(file.listFiles() ?: emptyArray())
        }
    }
    return total
}

/** Todokanai
 * @return a fileTree from [currentPath]
 * */
fun dirTree_td(currentPath: File): List<File> {
    val result = mutableListOf<File>()
    var now = currentPath
    while (now.parentFile != null) {
        result.add(now)
        now = now.parentFile!!
    }
    return result.reversed()
}

fun zipFileEntrySize_td(file: java.util.zip.ZipFile): Long {
    var result = 0L

    val entries = file.entries()
    while (entries.hasMoreElements()) {
        val entry = entries.nextElement() as ZipEntry
        result += entry.size
    }
    return result
}

/** Todokanai */
fun compressFilesRecursivelyToZip_td(files: Array<File>, zipFile: File) {
    val buffer = ByteArray(1024)
    val zipOutputStream = ZipOutputStream(zipFile.outputStream())

    fun addToZip(file: File, parentPath: String = "") {
        val entryName = if (parentPath.isNotEmpty()) "$parentPath/${file.name}" else file.name

        if (file.isFile) {
            val zipEntry = ZipEntry(entryName)
            zipOutputStream.putNextEntry(zipEntry)

            val inputStream = FileInputStream(file)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                zipOutputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            zipOutputStream.closeEntry()
        } else if (file.isDirectory) {
            val files = file.listFiles()
            files?.forEach { childFile ->
                addToZip(childFile, entryName)
            }
        }
    }
    for (file in files) {
        addToZip(file)
    }
    zipOutputStream.close()
    println("파일 압축이 완료되었습니다.")
}

/** Todokanai
 *
 * 경로가 접근 가능할 경우 true 반환
 * **/
fun isAccessible_td(file: File): Boolean {
    return file.listFiles() != null
}

/**
 * Function to get the MimeType from a filename by comparing it's file extension
 * @author Neeyat Lotlikar
 * @param filename String name of the file. Can also be a path.
 * @return String MimeType */
fun getMimeType_td(filename: String): String = if (filename.lastIndexOf('.') == -1)
    "resource/folder"
else
    when (filename.subSequence(
        filename.lastIndexOf('.'),
        filename.length
    ).toString().lowercase(Locale.ROOT)) {
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

/** @param filePath absolutePath string of file
 * @return absolutePath of parent file. null if unable to.
 * **/
fun getParentAbsolutePath_td(filePath: String): String? {
    val regex = Regex("^(.*)[/\\\\][^/\\\\]+$") // 파일 경로에서 마지막 디렉터리 이전 부분을 추출
    val matchResult = regex.find(filePath)
    return matchResult?.groupValues?.get(1) // 첫 번째 그룹이 부모 디렉터리 경로
}

/** file 을 확장자 없이 생성했을 경우, regex 필터가 걸러내지 못할 수 있음에 주의
 * @param filePath absolutePath of file
 * @return true if the file is a directory, else false
 * **/
fun isDirectoryByRegex_td(filePath: String): Boolean =
    Regex(".+[\\\\/]$").matches(filePath)    // // 경로가 '/' 또는 '\'로 끝나면 directory 로 판단

/** @param server server ip
 *  @param username login id
 *  @param password login password
 *  @param localFilePath absolutePath of the local file to upload
 *  @param remoteFilePath absolutePath of remote file to be uploaded **/
fun uploadFileToFtp_td(
    server: String, // server ip
    username: String,
    password: String,
    localFilePath: String, // 로컬 파일 경로
    remoteFilePath: String, // nas 파일 저장 경로
    port: Int = 21    // port (default = 21)
): Boolean {
    val ftpClient = FTPClient()
    return try {
        // FTP 서버 연결
        ftpClient.run {
            connect(server, port)
            login(username, password)
            enterLocalPassiveMode() // Passive Mode 사용 (방화벽 문제 방지)
            setFileType(FTP.BINARY_FILE_TYPE) // 바이너리 파일 전송
        }

        val localFile = File(localFilePath)
        FileInputStream(localFile).use { inputStream ->
            val uploaded = ftpClient.storeFile(remoteFilePath, inputStream)
            if (uploaded) {
                println("파일 업로드 성공: $remoteFilePath")
            } else {
                println("파일 업로드 실패")
            }
            uploaded
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
        println("FTP 연결 또는 업로드 중 오류 발생: ${ex.message}")
        false
    } finally {
        try {
            ftpClient.logout()
            ftpClient.disconnect()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}

/** @param server server ip
 *  @param username login id
 *  @param password login password
 *  @param localFilePath absolutePath of the local file to be downloaded
 *  @param remoteFilePath absolutePath of remote file to download **/
fun downloadFileFromFtp_td(
    server: String,
    username: String,
    password: String,
    remoteFilePath: String,
    localFilePath: String,
    port: Int = 21
): Boolean {
    val ftpClient = FTPClient()
    return try {
        // FTP 서버 연결
        ftpClient.run {
            connect(server, port)
            login(username, password)
            enterLocalPassiveMode() // Passive Mode 사용
            setFileType(FTP.BINARY_FILE_TYPE) // 바이너리 파일 전송
        }

        val localFile = File(localFilePath)
        FileOutputStream(localFile).use { outputStream ->
            val success = ftpClient.retrieveFile(remoteFilePath, outputStream)
            if (success) {
                println("파일 다운로드 성공: $localFilePath")
            } else {
                println("파일 다운로드 실패")
            }
            success
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
        println("FTP 연결 또는 다운로드 중 오류 발생: ${ex.message}")
        false
    } finally {
        try {
            ftpClient.logout()
            ftpClient.disconnect()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}

/** @param server server ip
 *  @param username login id
 *  @param password login password
 *  @param remoteDirectory absolutePath of remote directory to read
 *  @return contents of the given directory **/
fun listFilesInFtpDirectory_td(
    server: String,
    username: String,
    password: String,
    remoteDirectory: String,
    port: Int = 21
): Array<FTPFile> {
    val ftpClient = FTPClient()
    var result = emptyArray<FTPFile>()
    try {
        // FTP 서버 연결
        ftpClient.run {
            connect(server, port)
            login(username, password)
            enterLocalPassiveMode() // Passive Mode 사용
        }

        // 특정 디렉토리 내 파일 목록 가져오기
        result = ftpClient.listFiles(remoteDirectory)

        println("파일 목록 불러오기 성공: $result")
    } catch (ex: IOException) {
        ex.printStackTrace()
        println("파일 목록을 가져오는 중 오류 발생: ${ex.message}")
    } finally {
        try {
            ftpClient.logout()
            ftpClient.disconnect()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
    return result
}

/** @param client FTPClient to Login with
 * @return true if login is successful. else false **/
fun loginToFTPServer_td(
    client: FTPClient,
    serverIp: String,
    username: String,
    password: String,
    port: Int
): Boolean {
    var result: Boolean
    client.run {
        connect(serverIp, port)
        result = login(username, password)
        enterLocalPassiveMode() // Passive Mode 사용
    }
    return result
}