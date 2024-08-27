package com.todokanai.filemanager.compose.bottommenucontent

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.todokanai.filemanager.viewmodel.compose.BottomConfirmMenuViewModel

@Composable
fun BottomConfirmMenu(
    modifier: Modifier = Modifier,
    viewModel: BottomConfirmMenuViewModel = hiltViewModel(),
){

    Row (
        modifier = modifier
            .wrapContentSize()
    ){
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = { viewModel.cancel() }
        ) {
            Text(text="Cancel")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {viewModel.confirm() }
        ) {
            Text(text="Confirm")
        }

    }
}

@Preview
@Composable
private fun BottomConfirmMenuPreview(){
    BottomConfirmMenu()
}