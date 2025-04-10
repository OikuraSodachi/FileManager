package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.BooleanDialog

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    delete: () -> Unit
) {
    BooleanDialog(
        modifier = modifier,
        onConfirm = { delete() },
        onCancel = { onCancel() },
        title = stringResource(id = R.string.delete_dialog_title),
        message = stringResource(id = R.string.delete_dialog_message),
        confirmText = stringResource(id = R.string.delete_dialog_onConfirm),
        cancelText = stringResource(id = R.string.delete_dialog_onCancel)
    )
}