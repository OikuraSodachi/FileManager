package com.todokanai.filemanager.viewmodel.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Objects
import com.todokanai.filemanager.myobjects.Objects.fileModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BottomConfirmMenuViewModel @Inject constructor() : ViewModel() {

    private val model by lazy{ fileModel}
    private val modeManager by lazy{ Objects.modeManager}

    fun confirm(
        mode:Int = modeManager.selectMode.value,
        selectedFiles:Array<File> = modeManager.selectedFiles.value,
        target:File? = model.currentDirectory.value
        ){
        target?.let {
            viewModelScope.launch {
                when (mode) {
                    CONFIRM_MODE_COPY -> {
                        model.copyFiles(selectedFiles, it)

                    }

                    CONFIRM_MODE_UNZIP -> {
                        model.unzipFile(selectedFiles, it)

                    }

                    CONFIRM_MODE_MOVE -> {
                        model.moveFiles(selectedFiles, it)
                    }

                }
                modeManager.toDefaultSelectMode()

            }
        }
    }

    fun cancel(){
        modeManager.toDefaultSelectMode()
    }
}