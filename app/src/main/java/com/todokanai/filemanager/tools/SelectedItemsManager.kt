package com.todokanai.filemanager.tools

import java.io.File

/** Multi Select Mode 활성화 될때마다 새로운 Instance 생성되야함**/
class SelectedItemsManager {


    /** 선택된 file 목록 원본 **/
    val selectedItems = emptyList<File>().toMutableList()
}