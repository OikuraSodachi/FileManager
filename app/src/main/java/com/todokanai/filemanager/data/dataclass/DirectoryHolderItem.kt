package com.todokanai.filemanager.data.dataclass

data class DirectoryHolderItem(
    val name: String,
    /** 대응하는 File 을 특정할 수 있어야 함 **/
    val absolutePath: String
)
