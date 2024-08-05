package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ConfirmDelete(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    if (showDialog) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.WarningAmber, contentDescription = "Example Icon")
            },
            title = {
                Text(text = "Delete All Events?")
            },
            text = { Text(text = "Are you sure you want to delete your current roster completely? This action cannot be undone.")},
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }){
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Preview
@Composable
fun ConfirmDeletePreview(){
    ConfirmDelete(
        onConfirm = { /*TODO*/ }
        , onDismiss = { /*TODO*/ }
        , showDialog = true)
}