package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import java.io.File
import java.util.zip.ZipFile

class UnzipAction(
    val selectedZipFile:File,
    val targetDirectory:File
):FileAction {

    var progress : Int = 0
    private lateinit var currentFileInProcess : File

    override fun main() {
        unzip(ZipFile(selectedZipFile),targetDirectory)
    }

    override fun abort() {

     }

    override fun progressCallback() {

    }

    override fun onComplete() {
        myNoti.sendCompletedNotification("unzip completed",targetDirectory.absolutePath)
    }

    fun unzip(zipFile: ZipFile, targetDirectory: File) {
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs()
        }

        // zip 파일의 각 엔트리에 대해 처리
        zipFile.use { zip ->
            zip.entries().asSequence().forEach { entry ->
                // TODO: entry.isDirectory() == true 일 경우가 skip되고 있음. 크게 문제되진 않으나, 버그의 원인이 될 가능성이 있음(?)
                val outputFile = File(targetDirectory, entry.name)
                currentFileInProcess = outputFile
                println("current: ${outputFile.name}")
                // 디렉토리인 경우 디렉토리 생성
                if (entry.isDirectory) {
                    outputFile.mkdirs()
                } else {
                    // 디렉토리가 아닌 경우 파일을 추출
                    outputFile.parentFile.mkdirs()  // 부모 디렉토리가 없을 경우 생성
                    zip.getInputStream(entry).use { input ->
                        outputFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }
}