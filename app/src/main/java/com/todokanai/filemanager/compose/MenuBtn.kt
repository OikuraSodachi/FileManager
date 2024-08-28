package com.todokanai.filemanager.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.dialog.NewFolderDialog
import com.todokanai.filemanager.compose.dialog.SortDialog
import com.todokanai.filemanager.compose.presets.dropdownmenu.MyDropdownMenu

@Composable
fun MenuBtn(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    exit:()->Unit
){
    val shouldShowDialog = remember{mutableStateOf(false)}

    val newFolderDialog = remember{ mutableStateOf(false) }

    val contents : List<Pair<String,()->Unit>> = listOf(
        Pair(stringResource(id = R.string.menu_new_folder),{newFolderDialog.value = true}),
        Pair(stringResource(id = R.string.menu_sort),{shouldShowDialog.value = true}),
        Pair(stringResource(id = R.string.menu_exit),{exit()}),
    )

    MyDropdownMenu(
        modifier = modifier,
        contents = contents,
        expanded = expanded.value,
        onDismissRequest = {expanded.value = false}
    )

    if (shouldShowDialog.value) {
        SortDialog(
            modifier = Modifier,
            onDismissRequest = { shouldShowDialog.value = false })
    }

    if(newFolderDialog.value){
        NewFolderDialog(
            onCancel = { newFolderDialog.value = false },
            onConfirm = {}
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun MenuBtnPreview(){
    MenuBtn(
        expanded = mutableStateOf(true),
        exit = {}
    )
}