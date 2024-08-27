package com.todokanai.filemanager.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.todokanai.filemanager.compose.presets.dialog.CustomDialog
import com.todokanai.filemanager.viewmodel.compose.dialog.InfoDialogViewModel

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    onDismiss:()->Unit,
    viewModel: InfoDialogViewModel = hiltViewModel()
){
    val number = viewModel.selectedNumberText.collectAsStateWithLifecycle()
    val size = viewModel.sizeText.collectAsStateWithLifecycle()

    CustomDialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(text = "Selected")

            Text(text = number.value)

            Text(text = "Size")

            Text(text = size.value)

            //Text(text = "")

            

        }
    }
}

@Preview
@Composable
private fun InfoDialogPreview(){
    InfoDialog(
        onDismiss = {}
    )
}