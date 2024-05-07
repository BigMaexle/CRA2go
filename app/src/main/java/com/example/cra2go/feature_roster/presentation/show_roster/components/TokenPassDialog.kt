package com.example.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterAuthToken(
    onConfirmation:  (String) -> Unit,
    showTokenDialog: Boolean
) {
    var text by rememberSaveable { mutableStateOf("") }

    if (showTokenDialog) {


        AlertDialog(

            icon = {
                Icon(Icons.Default.Info, contentDescription = "Example Icon")
            },
            title = {
                Text(text = "Enter Token")
            },
            text = {

                Column() {
                    TextField(value = text, onValueChange = { newtext -> text = newtext })
                }

            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation(text)
                    }
                ) {
                    Text("Confirm")
                }
            }
        )
    }
}

@Preview
@Composable
fun DialogExamples() {
    // ...
    val openAlertDialog = remember { mutableStateOf(true) }

    // ...
    when {
        // ...
        openAlertDialog.value -> {
            EnterAuthToken(
                showTokenDialog = true,
                onConfirmation = {
                    openAlertDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                }
            )
        }
    }
}

