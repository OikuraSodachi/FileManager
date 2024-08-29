package com.todokanai.filemanager.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.todokanai.filemanager.compose.bottommenucontent.BottomConfirmMenu
import com.todokanai.filemanager.compose.bottommenucontent.BottomMultiSelectMenu

@Composable
fun BottomMenu(
    modifier:Modifier = Modifier,
    onConfirmTest:()->Unit,
    onCancel:()->Unit,
    move:()->Unit,
    copy:()->Unit,
    isMultiSelectMode:Boolean
){
    if(isMultiSelectMode) {
        BottomMultiSelectMenu(
            modifier = modifier,
            move = { move() },
            copy = { copy() },
            //  enablePopupMenu = {modeManager.selectedFiles.value.isNotEmpty()}
            enablePopupMenu = { true }
        )
    } else{
        BottomConfirmMenu(
            modifier = modifier,
            onCancel = {onCancel()},
            onConfirm = {onConfirmTest()}
        )
    }
}
