package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.EditTextDialog
import com.todokanai.filemanager.viewmodel.compose.dialog.NewFolderDialogViewModel

@Composable
fun NewFolderDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    viewModel: NewFolderDialogViewModel = hiltViewModel(),
){
    EditTextDialog(
        modifier = modifier,
        title = stringResource(id = R.string.new_folder_dialog_title),
        defaultText = stringResource(id = R.string.new_folder_dialog_default_text),
        onConfirm = {viewModel.confirm(it)},
        onCancel = {onCancel()}
    )

}