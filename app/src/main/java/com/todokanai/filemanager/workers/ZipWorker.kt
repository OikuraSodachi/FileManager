package com.todokanai.filemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.todokanai.filemanager.myobjects.Constants
import com.todokanai.filemanager.myobjects.Objects.myNoti
import com.todokanai.filemanager.tools.independent.getTotalSize_td
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** TODO: 안드로이드 기본 파일관리자에 비해서 압축 속도가 느린듯 함 **/
class ZipWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    var bytesDone: Long = 0L
    var prevProgress: Int = 0

    private val stringArray = inputData.getStringArray(Constants.WORKER_KEY_SELECTED_FILES)!!
    private val targetDirectoryName = inputData.getString(Constants.WORKER_KEY_TARGET_DIRECTORY)!!
    private val selectedFiles = stringArray.map { File(it) }.toTypedArray()
    private val targetDirectory = File(targetDirectoryName)

    val totalSize = getTotalSize_td(selectedFiles)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val startTime = System.currentTimeMillis()
            println("startTime: ${SimpleDateFormat("mm:ss").format(startTime)}")

            println("started")
            println("selected: ${selectedFiles.map { it.name }}")
            println("targetZipFile: ${targetDirectory.absolutePath}")

            fun callback(processed: Long) {
                val progress = (processed * 100 / totalSize).toInt()
                if (prevProgress != progress) {
                    println("progress: $progress")
                    println("bytes: $processed")
                    myNoti.sendSilentNotification("titleText", "${progress}%")
                    prevProgress = progress
                }
            }

            zipFilesWithProgress(
                selected = selectedFiles,
                target = targetDirectory,
                progressCallback = { callback(it) }
            )


            val endTime = System.currentTimeMillis()
            println("time: ${SimpleDateFormat("mm:ss").format(endTime - startTime)}")

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(totalSize, bytesDone)
    }

    private fun createForegroundInfo(max: Long, progress: Long): ForegroundInfo {
        //  println("max:$max, progress:$progress")
        val test = myNoti.ongoingNotiTest
            .setProgress(max.toInt(), progress.toInt(), false)
            .setContentText("$progress/$max")
        return ForegroundInfo(0, test.build())
    }

    fun zipFilesWithProgress(
        selected: Array<File>,
        target: File,
        progressCallback: (Long) -> Unit
    ) {
        ZipOutputStream(FileOutputStream(target)).use { zipOut ->
            selected.forEach { file ->
                addFileToZip(
                    zipOut = zipOut,
                    fileToZip = file,
                    fileName = file.name,
                    progressCallback = {
                        bytesDone += it
                        progressCallback(bytesDone)
                    }
                )
            }
        }
    }

    fun addFileToZip(
        zipOut: ZipOutputStream,
        fileToZip: File,
        fileName: String,
        progressCallback: (Long) -> Unit
    ) {
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
                if (fileProcessedSize % (bytes.size) == 0L) {
                    progressCallback(length.toLong())
                }
            }
            zipOut.closeEntry()
        }
    }
}