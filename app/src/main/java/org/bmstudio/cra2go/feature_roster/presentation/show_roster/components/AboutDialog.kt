package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.bmstudio.cra2go.BuildConfig

@Composable
fun AboutDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
){

    if (showDialog){
        AlertDialog(
            icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
            onDismissRequest = { onDismiss() },
            confirmButton =     { TextButton(onClick = {onDismiss()}) { Text(text="OK")       } },
            title = { Text(text = "About")},
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "CRA2go - compact offline roster \nversion: ${BuildConfig.VERSION_NAME}\ncreated by Maximilian Wittorf \n") })
    }

}

@Preview
@Composable
fun AboutDialogPreview(){
    AboutDialog(showDialog = true, onDismiss = {})
}