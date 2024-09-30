package com.todokanai.filemanager.compose.bottommenucontent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.dialog.DeleteDialog
import com.todokanai.filemanager.compose.dialog.UnzipDialog
import com.todokanai.filemanager.compose.dialog.ZipDialog
import java.io.File

@Composable
fun BottomMultiSelectMenu(
    modifier: Modifier = Modifier,
    move:()->Unit,
    copy:()->Unit,
    delete:()->Unit,
    zip:(String)->Unit,
    unzip:(String)->Unit,
    selected:Array<File>
){
    val expandedState = remember { mutableStateOf(false) }
    val deleteDialog = remember{mutableStateOf(false)}
    val zipDialog = remember { mutableStateOf(false) }
    val unzipDialog = remember { mutableStateOf(false) }

    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { move() }
        ) {
            Text(stringResource(id = R.string.bottom_multi_select_menu_move))
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { copy() }
        ) {
            Text(stringResource(id = R.string.bottom_multi_select_menu_copy))
        }
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { deleteDialog.value = true }
        ) {
            Text(stringResource(id = R.string.bottom_multi_select_menu_delete))
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                if(expandedState.value == false) {
                    expandedState.value = true
                }
            }
        ) {
            Text(stringResource(id = R.string.bottom_multi_select_menu_more))
            BottomPopupMenu(
                expanded = expandedState,
                zip = {zipDialog.value = true},
                unzip ={unzipDialog.value = true},
                selectAll = {},
                unselectAll = {},
                selected = selected
            )
        }
    }

    if(deleteDialog.value){
        DeleteDialog(
            onCancel = {deleteDialog.value = false},
            delete = {delete()}
        )
    }

    if(zipDialog.value){
        ZipDialog(
            onCancel = {zipDialog.value = false},
            onConfirm = {zip(it)}
        )
    }

    if(unzipDialog.value){
        UnzipDialog(
            onCancel = { unzipDialog.value = false },
            onConfirm = {unzip(it)}
        )
    }
}

@Preview
@Composable
private fun BottomMultiSelectMenuPreview(){
    BottomMultiSelectMenu(
        copy = {},
        move = {},
        delete = {},
        zip = {},
        unzip = {},
        selected = emptyArray()
    )
}