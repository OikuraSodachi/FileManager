package com.todokanai.filemanager.tools.actions

import com.todokanai.filemanager.interfaces.FileAction
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipAction(
    val selectedFiles:Array<File>,
    val targetZipFile:File,
):FileAction {

    var bytesDone : Long = 0L
    var prevProgress: Int = 0
    val totalSize = selectedFiles.getTotalSize_td()

    override fun start() {

        ZipOutputStream(FileOutputStream(targetZipFile)).use { zipOut ->
            selectedFiles.forEach { file ->
                addFileToZip(
                    zipOut = zipOut,
                    fileToZip = file,
                    fileName = file.name,
                    progressCallback ={
                        bytesDone += it
                        progressCallback(bytesDone)
                    }
                )
            }
        }
    }

    override fun abort() {

    }

    override fun progressCallback(processedBytes: Long) {
        val progress = (processedBytes * 100 / totalSize).toInt()
        if (prevProgress != progress) {
            println("progress: $progress")
            println("bytes: $processedBytes")
        //    myNoti.sendSilentNotification("titleText","${progress}%")
            myNoti.sendProgressNotification("titleText","${progress}%",progress)
            prevProgress = progress
        }
    }

    override fun onComplete() {
        TODO("Not yet implemented")
    }

    fun addFileToZip(zipOut: ZipOutputStream, fileToZip: File, fileName: String, progressCallback: (Long) -> Unit) {
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
}