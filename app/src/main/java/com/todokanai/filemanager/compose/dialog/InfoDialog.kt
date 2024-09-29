package com.todokanai.filemanager.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.CustomDialog

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    onDismiss:()->Unit,
    numberText:String,
    sizeText:String
){
    CustomDialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.info_dialog_selected))

            Text(text = numberText)

            Text(stringResource(id = R.string.info_dialog_size))

            Text(text = sizeText)

            //Text(text = "")

        }
    }
}

@Preview
@Composable
private fun InfoDialogPreview(){
    InfoDialog(
        onDismiss = {},
        numberText = "numberText",
        sizeText = "sizeText"
    )
}