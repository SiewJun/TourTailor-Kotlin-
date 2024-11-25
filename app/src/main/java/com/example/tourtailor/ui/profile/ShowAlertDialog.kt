package com.example.tourtailor.ui.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ShowAlertDialog(
    showAlertDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    text: String
) {
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = { Button(onClick = onDismiss) { Text("OK") } }
        )
    }
}
