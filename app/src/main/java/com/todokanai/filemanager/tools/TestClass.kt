package com.todokanai.filemanager.tools

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class TestClass {
    fun zipFilesWithProgress(selected: Array<File>, target: File,progressCallback:(Long)->Unit) {
        val totalSize = selected.sumOf { calculateFileSize(it) } // 전체 파일 크기 계산
        var processedSize = 0L

        ZipOutputStream(FileOutputStream(target)).use { zipOut ->
            selected.forEach { file ->
                /*
                addFileToZip(zipOut, file, file.name) { fileSize ->
                    processedSize += fileSize
                    val progress = (processedSize.toDouble() / totalSize * 100).toInt()
                    println("Progress: $progress%")
                }
                 */

                addFileToZip(
                    zipOut = zipOut,
                    fileToZip = file,
                    fileName = file.name,
                    progressCallback ={
                        processedSize += it
                 //       val progress = (processedSize.toDouble() / totalSize * 100).toInt()
                        progressCallback(processedSize)
                    }
                )
            }
        }
    }

    fun addFileToZip(zipOut: ZipOutputStream, fileToZip: File, fileName: String, progressCallback: (Long) -> Unit) {
        if (fileToZip.isHidden) {
            return
        }
        if (fileToZip.isDirectory) {
            val children = fileToZip.listFiles() ?: return
            for (childFile in children) {
                addFileToZip(zipOut, childFile, "$fileName/${childFile.name}", progressCallback)
            }
            return
        }

        FileInputStream(fileToZip).use { fis ->
            val zipEntry = ZipEntry(fileName)
            zipOut.putNextEntry(zipEntry)

            val bytes = ByteArray(1024)
            var length: Int
            var fileProcessedSize = 0L

            while (fis.read(bytes).also { length = it } >= 0) {
                zipOut.write(bytes, 0, length)
                fileProcessedSize += length
                if(fileProcessedSize%(bytes.size) == 0L) {
                    progressCallback(length.toLong())
                }
            }
            zipOut.closeEntry()
        }
    }

    fun calculateFileSize(file: File): Long {
        return if (file.isDirectory) {
            file.listFiles()?.sumOf { calculateFileSize(it) } ?: 0L
        } else {
            file.length()
        }
    }

}