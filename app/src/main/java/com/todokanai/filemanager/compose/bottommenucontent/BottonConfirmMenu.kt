package com.todokanai.filemanager.compose.bottommenucontent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomConfirmMenu(
    modifier: Modifier,
    onCancel:()->Unit,
    onConfirm:()->Unit
){
    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { onCancel() }
        ) {
            Text(text="Cancel")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { onConfirm() }
        ) {
            Text(text="Confirm")
        }

    }
}

@Preview
@Composable
private fun BottomConfirmMenuPreview(){
    BottomConfirmMenu(
        modifier = Modifier,
        onCancel = {},
        onConfirm = {}
    )
}