package com.todokanai.filemanager.tools

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class Tools(val context: Context) {
    
    fun makeShortToast(message:String){
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show() },0)
    }       // Coroutine 내부에서 Toast를 띄우기



}