package com.todokanai.filemanager.myobjects

import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
/** 기능 구현을 위해 일단 companion object 로 선언한 변수들. 나중에 이동시킬 것 ( memory leak )**/
class Variables {

    companion object {
        /** Todo: 얘 여기에 배치하는게 맞는지? **/
        val storages = MutableStateFlow<Array<File>>(emptyArray())

        /** 아마 [com.todokanai.filemanager.adapters.ViewPagerAdapter] 에 배치해야 할 듯? **/
        val selectedItems = MutableStateFlow<Array<String>>(emptyArray())

    }
}