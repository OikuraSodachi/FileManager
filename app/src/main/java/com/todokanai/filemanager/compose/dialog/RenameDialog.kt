package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.EditTextDialog
import com.todokanai.filemanager.viewmodel.compose.dialog.RenameDialogViewModel

@Composable
fun RenameDialog(
    modifier: Modifier = Modifier,
    onCancel:()->Unit,
    viewModel:RenameDialogViewModel = hiltViewModel()
){
    EditTextDialog(
        modifier = modifier,
        title = stringResource(id = R.string.rename_dialog_title),
        defaultText = stringResource(id = R.string.rename_dialog_default_text),
        onConfirm = {viewModel.onConfirm(it)},
        onCancel = {onCancel()}
    )
}