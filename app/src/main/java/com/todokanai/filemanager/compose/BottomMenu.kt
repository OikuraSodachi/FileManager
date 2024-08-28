package com.todokanai.filemanager.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.todokanai.filemanager.compose.bottommenucontent.BottomConfirmMenu
import com.todokanai.filemanager.compose.bottommenucontent.BottomMultiSelectMenu
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.filemanager.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.filemanager.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.filemanager.tools.SelectModeManager

@Composable
fun BottomMenu(
    modifier:Modifier = Modifier,
    selectMode: Int,
    modeManager: SelectModeManager
){
    when(selectMode){
        MULTI_SELECT_MODE->{
            BottomMultiSelectMenu(
                modifier = modifier,
                move = {modeManager.toConfirmMoveMode()},
                copy = {modeManager.toConfirmCopyMode()},
                enablePopupMenu = {modeManager.selectedFiles.value.isNotEmpty()}
            )
        }
        CONFIRM_MODE_COPY->{
            BottomConfirmMenu(
                modifier = modifier,
                onCancel = {modeManager.toDefaultSelectMode()},
                onConfirm = {}
            )
        }
        CONFIRM_MODE_MOVE->{
            BottomConfirmMenu(
                modifier = modifier,
                onCancel = {modeManager.toDefaultSelectMode()},
                onConfirm = {}
            )
        }
        CONFIRM_MODE_UNZIP->{
            BottomConfirmMenu(
                modifier = modifier,
                onCancel = {modeManager.toDefaultSelectMode()},
                onConfirm = {}
            )
        }
        CONFIRM_MODE_UNZIP_HERE->{
            BottomConfirmMenu(
                modifier = modifier,
                onCancel = {modeManager.toDefaultSelectMode()},
                onConfirm = {}
            )
        }
        else->{
            println("select mode error: $selectMode")
        }
    }
}