package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class UnzipAction(
    val selectedZipFile:File,
    val targetDirectory:File
):FileAction {

    var progress : Int = 0
    private lateinit var currentFileInProcess : File

    override fun main() {
        extractZipFile(
            selectedZipFile,
            targetDirectory
        )

    }

    override fun abort() {

     }

    override fun progressCallback() {

    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("unzip completed",targetDirectory.absolutePath)
    }

    fun extractZipFile(file: File, target: File) {
        val bufferSize = 4096 // 압축 해제할 때 사용할 버퍼 크기

        val zipInputStream = ZipInputStream(FileInputStream(file))
        var entry: ZipEntry? = zipInputStream.nextEntry

        while (entry != null) {
            val entryName = entry.name // 압축 해제될 파일 또는 폴더명
            val entryPath = target.toPath().resolve(entryName)

            if (entry.isDirectory) {
                // 폴더인 경우 폴더 생성
                Files.createDirectories(entryPath)
            } else {
                // 파일인 경우 파일 추출
                println("Extracting: $entryName")

                val output = entryPath.toFile().outputStream()

                var read: Int
                val buffer = ByteArray(bufferSize)

                while (zipInputStream.read(buffer, 0, bufferSize).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }

                output.close()
            }

            zipInputStream.closeEntry()
            entry = zipInputStream.nextEntry
        }

        zipInputStream.close()
    }
}