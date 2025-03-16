package com.todokanai.filemanager.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun NASDialog(
    title: String,
    onConfirm: (ip:String,id:String,password:String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    cancelText:String = "Cancel",
    confirmText:String = "Confirm"
){
    var ip by remember{ mutableStateOf("") }
    var id by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(text = title) },
        text = {
            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = ip,
                    placeholder = { Text("ip") },
                    onValueChange = { ip = it },
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = id,
                    placeholder = { Text("id") },
                    onValueChange = { id = it },
                    singleLine = true
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    placeholder = { Text("password") },
                    onValueChange = { password = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(ip, id, password)
                    onCancel()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancel()
                }
            ) {
                Text(text = cancelText)
            }
        }
    )
}