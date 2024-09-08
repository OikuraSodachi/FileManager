package com.todokanai.filemanager.compose.bottommenucontent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Objects
import java.io.File

@Composable
fun BottomConfirmMenu(
    modifier: Modifier,
    onCancel:()->Unit,
    copyWork:(Array<File>,File)->Unit,
    moveWork:(Array<File>,File)->Unit,
    selected:Array<File>,
    getDirectory:()->File
){
    val modeManager = Objects.modeManager
    fun onConfirm(mode:Int){
        when(mode){
            CONFIRM_MODE_COPY -> {
                copyWork(selected,getDirectory())
            }

            CONFIRM_MODE_MOVE -> {
                moveWork(selected,getDirectory())
            }

            CONFIRM_MODE_UNZIP -> {

            }

            CONFIRM_MODE_UNZIP_HERE -> {

            }
        }
        modeManager.onDefaultMode_new()
    }

    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { onCancel() }
        ) {
            Text(text="Cancel")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {onConfirm(modeManager.selectMode())}
        ) {
            Text(text="Confirm")
        }

    }
}

@Preview
@Composable
private fun BottomConfirmMenuPreview(){
    BottomConfirmMenu(
        modifier = Modifier,
        onCancel = {},
        copyWork = { array,target->{} },
        moveWork = {array,target->{}},
        selected = emptyArray(),
        getDirectory = {File("")}
    )
}