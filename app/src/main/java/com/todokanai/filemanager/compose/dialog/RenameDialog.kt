package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.EditTextDialog

@Composable
fun RenameDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    //viewModel:RenameDialogViewModel = hiltViewModel()
    onConfirm: (String) -> Unit
) {
    EditTextDialog(
        modifier = modifier,
        title = stringResource(id = R.string.rename_dialog_title),
        defaultText = stringResource(id = R.string.rename_dialog_default_text),
        onConfirm = { onConfirm(it) },
        onCancel = { onCancel() }
    )
}