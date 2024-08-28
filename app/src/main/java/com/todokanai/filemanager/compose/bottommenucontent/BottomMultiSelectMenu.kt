package com.todokanai.filemanager.compose.bottommenucontent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.filemanager.compose.dialog.DeleteDialog

/*
@Composable
fun BottomMultiSelectMenu(
    modifier: Modifier = Modifier,
    viewModel: BottomMultiSelectMenuViewModel = hiltViewModel(),
){
    val expandedState = remember { mutableStateOf(false) }
    val deleteDialog = remember{mutableStateOf(false)}

    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { viewModel.move() }
        ) {
            Text(text="Move")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { viewModel.copy() }
        ) {
            Text(text="Copy")
        }
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { deleteDialog.value = true }
        ) {
            Text(text="Delete")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                if(viewModel.enablePopupMenu()) {
                    expandedState.value = true
                }
            }
        ) {
            Text(text="More")
            BottomPopupMenu(
                expanded = expandedState
            )
        }
    }

    if(deleteDialog.value){
        DeleteDialog(
            onCancel = {deleteDialog.value = false}
        )
    }
}

 */

@Composable
fun BottomMultiSelectMenu(
    modifier: Modifier = Modifier,
    move:()->Unit,
    copy:()->Unit,
    enablePopupMenu:()->Boolean
){
    val expandedState = remember { mutableStateOf(false) }
    val deleteDialog = remember{mutableStateOf(false)}

    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { move() }
        ) {
            Text(text="Move")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { copy() }
        ) {
            Text(text="Copy")
        }
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { deleteDialog.value = true }
        ) {
            Text(text="Delete")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                if(enablePopupMenu()) {
                    expandedState.value = true
                }
            }
        ) {
            Text(text="More")
            BottomPopupMenu(
                expanded = expandedState,
                zip = {},
                selectAll = {},
                unselectAll = {},
                selected = { emptyArray() }
            )
        }
    }

    if(deleteDialog.value){
        DeleteDialog(
            onCancel = {deleteDialog.value = false},
            delete = {}
        )
    }
}

@Preview
@Composable
private fun BottomMultiSelectMenuPreview(){
    BottomMultiSelectMenu(
        copy = {},
        move = {},
        enablePopupMenu = {true}
    )
}