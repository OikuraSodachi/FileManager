package com.todokanai.filemanager.tools

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.todokanai.filemanager.BuildConfig
import com.todokanai.filemanager.R
import com.todokanai.filemanager.application.MyApplication
import com.todokanai.filemanager.mydialog.MyDialogModel
import com.todokanai.filemanager.myobjects.Constants.CHANNEL_ID
import com.todokanai.filemanager.myobjects.Constants.DEFAULT_MODE
import com.todokanai.filemanager.myobjects.MyObjects
import com.todokanai.filemanager.notification.MyNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class FileAction (){
    private val model = FileActionModel()
    private val myContext = MyApplication.appContext
    private val tool = Tools(myContext)
    private val myNoti = MyNotification(CHANNEL_ID, R.drawable.ic_launcher_background)
    private fun dModel(context: Context) = MyDialogModel(context)

    val selectMode = MyObjects.selectMode
    val fileList = MyObjects.fileList
    val currentPath = MyObjects.currentPath
    val storageList = MyObjects.physicalStorageList

    fun renameAction(context: Context,selectedItem:File){
        val builder = AlertDialog.Builder(context)
        val textInput = EditText(context)
        textInput.gravity = Gravity.CENTER
        builder.setTitle("Rename")
            .setMessage("New Name")
            .setView(textInput)
            .setPositiveButton("Confirm",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "확인 클릭"
                    model.renameFileOrFolder(selectedItem, textInput.text.toString())
                    makeShortToast("Renamed to ${textInput.text.toString()}")
                    selectMode.value = DEFAULT_MODE
                    updateContents()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "취소 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun createFolderAction(context: Context,path: String){
        val builder = AlertDialog.Builder(context)
        val textInput = EditText(context)
        textInput.gravity = Gravity.CENTER
        builder.setTitle("New Folder")
            .setMessage("Folder Name")
            .setView(textInput)
            .setPositiveButton("Confirm",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "확인 클릭"
                    /*
                    if(model.filesInPath(path).contains(File(textInput.text.toString()))){

                     */
                    if(File(textInput.text.toString()).exists()) {
                        // 이미 이름이 존재할 경우
                        // 대충 positiveButton disable?
                    } else {
                        model.createFolder(path, textInput.text.toString())
                        makeShortToast("${textInput.text} Created")
                        updateContents()
                    }
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "취소 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun openAction(context: Context, selectedFile: File){
        if (selectedFile.isDirectory) {
            openFolder(selectedFile)
        } else {
            model.openFile(context, selectedFile, BuildConfig.APPLICATION_ID)
        }
    }

    fun copyAction(selectedList:List<File>, path:String){
        CoroutineScope(Dispatchers.IO).launch {
            makeShortToast("Copying")
            copyFiles(selectedList, path)
        }.invokeOnCompletion {
            makeShortToast("Copied")
            myNoti.createNotification("Copied", "${selectedList.size} Files")
            updateContents()
        }
    }


    fun deleteAction(context: Context,selectedFiles: Set<File>){

        CoroutineScope(Dispatchers.IO).launch {
            if(   dModel(context).booleanReturnDialog("delete?","delete?","confirm","cancel")
            ) {
                deleteFiles(selectedFiles.toList())
                makeShortToast("Deleting")
            }
        }.invokeOnCompletion {
            makeShortToast("Deleted")
            myNoti.createNotification("Deleted", "${selectedFiles.size} Files")
            selectMode.value = DEFAULT_MODE
            updateContents()
        }
    }

    fun moveAction(selectedList: List<File>, path: String){
        CoroutineScope(Dispatchers.IO).launch {
            makeShortToast("Moving")
            moveFiles(selectedList, path)
        }.invokeOnCompletion {
            myNoti.createNotification("Moved","Moved")
            makeShortToast("Moved")
            updateContents()
        }
    }

    fun zipAction(files:List<File>,zipFile:File) {
        CoroutineScope(Dispatchers.IO).launch {
            makeShortToast("compressing")
            model.compressToZip(files, zipFile)
        }.invokeOnCompletion {
            makeShortToast("compressed to ${zipFile.name}")
            updateContents()
        }
    }

    fun unzipAction(file:File,targetPath:String) {
        CoroutineScope(Dispatchers.IO).launch {
            println("Unzip : ${file.extension}")
            when(file.extension){
                "zip" -> model.unZip(file, targetPath)
             //   "RAR" -> model.unRar(file, targetPath)
            }
        }.invokeOnCompletion {
            makeShortToast("Unzipped to $targetPath")
            updateContents()
        }
    }

    //-------------------


    suspend fun getThumbnail(file: File):Bitmap {
        return model.getThumbnail(file)
    }

    fun updateContents() {
        fileList.value = File(currentPath.value).listFiles() as Array<File>
    }

    fun updateStorageList(storages:List<String>){
        storageList.value = storages
    }

    fun updateSelectedList(selectedList:MutableSet<File>, selectedFile: File){
        if(selectedList.contains(selectedFile)){
            selectedList.remove(selectedFile)
        } else{
            selectedList.add(selectedFile)
        }
        Log.d("FileAction","selectedList: $selectedList")
    }           // 선택 목록 selectedList에 대해서 추가/제거 동작

    private fun openFolder(selectedFile: File) {
        currentPath.value = selectedFile.absolutePath
    }

    fun fileSizeFormat(size: Long) = model.readableFileSize(size)

    fun fileTree(file: File) = model.fileTree(file)

    fun info(context: Context, selectedList:List<File>){
        if(selectedList.isNotEmpty()) {
            MyDialogModel(context).infoDialog(selectedList)
        }
    }
    fun getReadableTotalSize(files:List<File>) = model.readableFileSize(model.getTotalSize(files))

    private fun makeShortToast(message:String) = tool.makeShortToast(message)

    private suspend fun copyFiles( files:List<File>, path: String){
        val total : Long = model.getTotalSize(files)
        println("totalOut: ${model.readableFileSize(total)}")
        if(File(path).freeSpace<=total){
            makeShortToast("Not enough Space ()")
        } else {
            model.copyFile(files, path, { fileName, bytesCopied ->
                myNoti.progressNoti(
                    fileName,
                    "${(100 * bytesCopied / total).toInt()}%",
                    (100 * bytesCopied / total).toInt()
                ) }
            )
        }
    }


    private suspend fun moveFiles(files:List<File>, path: String){
        val total : Long = model.getTotalSize(files)
        println("total: $total")
        if(File(path).freeSpace<=total){
            makeShortToast("Not enough Space ()")
        } else {
            moveFileWrapper(files, path) { fileName, bytesCopied ->
                println("copied: $bytesCopied")

                println("ratio: ${(bytesCopied*100)/total}")
                myNoti.progressNoti(
                    fileName,
                    "${(100 * bytesCopied / total).toInt()}%",
                    (100 * bytesCopied / total).toInt()
                )
            }
        }
    }

    private suspend fun deleteFiles(files:List<File>){
        val total : Long = model.getTotalSize(files)
        model.deleteFile(files, { fileName, bytesDeleted ->
            if(total>0L) {
                myNoti.progressNoti(
                    fileName,
                    "${(100 * bytesDeleted / total).toInt()}%",
                    (100 * bytesDeleted / total).toInt()
                )
            }
            })
    }


    private suspend fun moveFileWrapper(files: List<File>, path:String, myCallback: (fileName: String, bytesCopied: Long) -> Unit) = withContext(Dispatchers.IO){
        files.forEach { file ->
            if( model.physicalStorage(file) == model.physicalStorage(File(path)) ) {
                Files.move(Paths.get(file.absolutePath), Paths.get(path + "/${file.name}"))     // 같은 저장소일경우
            } else {
                model.copyFile(listOf(file),path,myCallback)
                model.deleteSingleFile(file)
            }
        }
    }



    private suspend fun overwriteConfirmation(context: Context) = MyDialogModel(context).booleanReturnDialog(
        "Same Name Exists", "Overwrite?",
        "confirm",
        "cancel"
    )

    private suspend fun overwriteMoveConfirmation(context: Context) = MyDialogModel(context).booleanReturnDialog(
        "Same Name Exists", "Proceed?",
        "confirm",
        "cancel"
    )



    }

