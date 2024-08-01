package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.domain.model.LoginActivityContract
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.verticalCalender
import org.bmstudio.cra2go.login.presentation.LoginActivity
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import java.text.SimpleDateFormat
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DutyEventScreen(
    viewModel: DutyEventViewModel = hiltViewModel(),
) {


    val state = viewModel.state.value
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showaboutdialog by remember { mutableStateOf(false) }
    var showexportdialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val title = if (BuildConfig.DEBUG) {"CRA2go - MOCK"} else {"CRA2go"}

    val LoginContract = rememberLauncherForActivityResult(
        contract = LoginActivityContract()
    ) { result ->
        if (result != null) {
            viewModel.onEvent(DutyEventsEvent.UpdateRoster(result,context))
        }
        else Toast.makeText(context, "no Result", Toast.LENGTH_SHORT).show()

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                
                Text(text = "CRA2go",
                    modifier = Modifier.padding(16.dp))
                Divider()


                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.Download, contentDescription = "Download")},
                    label = { Text(text = "Download Roster") },
                    selected = false,
                    onClick = {
                        LoginContract.launch(1)
                        scope.launch { drawerState.apply { close() } }
                    })

                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete") },
                    label = { Text(text = "Delete Duty Events") },
                    selected = false,
                    onClick = {
                        showDeleteDialog = true
                        scope.launch { drawerState.apply { close() } }
                    })


                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.Info, contentDescription = "Info") },
                    label = { Text(text = "About") },
                    selected = false,
                    onClick = {
                        showaboutdialog = true
                        scope.launch { drawerState.apply { close() } }
                    })



                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Made with ❤\uFE0F in BER \n v${BuildConfig.VERSION_NAME}",
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth(1f),
                    textAlign = TextAlign.Center,
                    )


            }
        },
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(text = title,modifier = Modifier.fillMaxWidth(1f),
                        textAlign = TextAlign.Center) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")

                }
            }
        ) { innerPadding ->

            verticalCalender(events = state.events, padding = innerPadding)

        }
    }



    ConfirmDelete(
        onConfirm = {
            showDeleteDialog = false
            viewModel.onEvent(DutyEventsEvent.DeleteAllEvents)
        },
        onDismiss = { showDeleteDialog = false }, showDialog = showDeleteDialog
    )

    AboutDialog(showDialog = showaboutdialog, onDismiss = {showaboutdialog = false})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
){

    if (showDialog){
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton =     { TextButton(onClick = {onDismiss()}) { Text(text="OK")       }},
            text = {Text(textAlign = TextAlign.Center, text = "CRA2go - compact offline roster \nversion: ${BuildConfig.VERSION_NAME}\ncreated by\nMaximilian Wittorf \n")})
    }

}

@Composable
fun ConfirmDelete(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean
) {
    if (showDialog) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.WarningAmber, contentDescription = "Example Icon")
            },
            text = { Text(text = "Delete all duty events?") },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text("Confirm")
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
fun DutyEventScreen_preview(){
    CRA2goTheme {
        //DutyEventScreen(LoginContract = ActivityResultLauncher {  })
    }
}


