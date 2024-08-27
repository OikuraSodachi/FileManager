package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.BooleanDialog
import com.todokanai.filemanager.viewmodel.compose.dialog.DeleteDialogViewModel

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    viewModel: DeleteDialogViewModel = hiltViewModel()
){
    BooleanDialog(
        modifier = modifier,
        onConfirm = { viewModel.delete() },
        onCancel = { onCancel() },
        title = stringResource(id = R.string.delete_dialog_title),
        message = stringResource(id = R.string.delete_dialog_message)
    )

}