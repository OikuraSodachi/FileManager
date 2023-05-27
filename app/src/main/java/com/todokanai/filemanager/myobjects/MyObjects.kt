package com.todokanai.filemanager.myobjects

import android.os.Environment
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

object MyObjects {
    val selectMode = MutableStateFlow<Int>(Constants.DEFAULT_MODE)      // selectMode 원본
    val currentPath = MutableStateFlow<String>(Environment.getExternalStorageDirectory().path)      // 현재 경로 변수 원본
    val fileList = MutableStateFlow<Array<File>>(emptyArray())      // 현재 파일 목록 변수 원본
    var physicalStorageList = MutableStateFlow<List<String>>(emptyList())       // 물리적 외부 저장소 목록

}