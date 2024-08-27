package com.todokanai.filemanager.compose.presets.dropdownmenu

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyDropdownMenu(
    modifier:Modifier = Modifier,
    contents:List<Pair<String,()->Unit>>,
    expanded : Boolean,
    onDismissRequest: () -> Unit
) {

    DropdownMenu(
        modifier = modifier.wrapContentSize(),
        expanded = expanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        contents.forEach {
            DropdownMenuItem(
                text = { Text(it.first) },
                onClick = { it.second() }
            )
        }
    }
}


@Preview
@Composable
private fun DropdownMenuPreview(){
    MyDropdownMenu(
        contents = listOf(Pair("1", {}), Pair("2", {}),Pair("3",{})),
        expanded = true,
        onDismissRequest = {}
    )
}

