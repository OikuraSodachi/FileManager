package com.todokanai.filemanager.mydialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.todokanai.filemanager.R
import com.todokanai.filemanager.databinding.InfoDialogBinding
import com.todokanai.filemanager.tools.FileAction
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File


class MyDialogModel(val context:Context) {

    private val fAction = FileAction()

    fun defaultAlertDialog(){

        val builder = AlertDialog.Builder(context)

       // val mView = 대충 뷰 종류
        // mView.gravity = Gravity.CENTER

        builder.setTitle("타이틀 입니다.")
            .setMessage("메세지 내용 부분 입니다.")
        //    .setView(mView)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "확인 클릭"

                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "취소 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }


    suspend fun booleanReturnDialog(title: String, message: String, positiveButtonTitle: String, negativeButtonTitle: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(title)
                builder.setMessage(message)
                builder.setPositiveButton(positiveButtonTitle) { dialog, which ->
                    continuation.resume(true,onCancellation = null) // 사용자가 확인 버튼을 눌렀으므로 true 반환
                }
                builder.setNegativeButton(negativeButtonTitle) { dialog, which ->
                    continuation.resume(false,onCancellation = null) // 사용자가 취소 버튼을 눌렀으므로 false 반환
                }
                builder.setOnCancelListener {
                    continuation.cancel() // 다이얼로그가 취소되었으므로 코루틴을 취소
                }
                builder.show() },0)
        }
    }

    fun editTextDialog(){
        val builder = AlertDialog.Builder(context)
        val et = EditText(context)
        et.gravity = Gravity.CENTER
        builder.setTitle("타이틀 입니다.")
            .setMessage("메세지 내용 부분 입니다.")
            .setView(et)
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "확인 클릭"
                    Toast.makeText(context,et.text, Toast.LENGTH_LONG).show()
                    val result = et.text

                    // 이 시점에서 result 가지고 폴더 생성 메소드 실행하기
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    //resultText.text = "취소 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun defaultCustomDialog(layout:Int){
        val mDialog = Dialog(context)
        mDialog.setContentView(layout)
        mDialog.show()
    }

    fun infoDialog(selectedList:List<File>){
        val mDialog = Dialog(context)
        val binding = InfoDialogBinding.inflate(mDialog.layoutInflater)
        if(selectedList.size == 1){
            binding.infoName.text = selectedList.first().name
        } else {
            binding.infoName.text = "${selectedList.first().name}, ${selectedList.size-1} more"
        }
        binding.infoSize.text = fAction.getReadableTotalSize(selectedList)
        binding.infoTypesandnumber.text = "Todo"
        mDialog.setContentView(binding.root)
        mDialog.show()
    }

    //
}

