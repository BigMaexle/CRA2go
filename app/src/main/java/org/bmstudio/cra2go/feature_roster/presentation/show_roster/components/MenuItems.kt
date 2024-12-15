package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.domain.model.LoginActivityContract
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventViewModel
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventsEvent
import org.bmstudio.cra2go.settings.presentation.SettingsActivity

@Composable
fun MenuItems(
    forceLogin: ForceLogin,
    drawerState: DrawerState,
    viewModel: DutyEventViewModel = hiltViewModel()
){
        val ctx = LocalContext.current
        val LoginContract = rememberLauncherForActivityResult(
            contract = LoginActivityContract()
        ) { result ->
            if (result != null) {
                viewModel.onEvent(DutyEventsEvent.UpdateRoster(result, ctx))
            }
        }


        val scope = rememberCoroutineScope()


        var showaboutdialog by remember { mutableStateOf(false) }

        ModalDrawerSheet (
            modifier = Modifier
                .fillMaxWidth(0.6f)
        ) {


            AboutDialog(
                showDialog = showaboutdialog,
                onDismiss = { showaboutdialog = false }
            )

            Column (modifier = Modifier.padding(10.dp)){
                Text(text = "CRA2go",
                    modifier = Modifier.padding(16.dp)
                )
                Divider()


                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.CloudDownload, contentDescription = "Download") },
                    label = { Text(text = "Download Roster") },
                    selected = false,
                    onClick = {


                        scope.launch {
                            if (forceLogin.get()) {
                                LoginContract.launch(1)
                            } else {
                                LoginContract.launch(0)
                            }
                            drawerState.apply { close() }
                        }
                   })


                Divider()

                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text(text = "Settings") },
                    selected = false,
                    onClick = {
                        val intent = Intent(ctx, SettingsActivity::class.java)
                        ctx.startActivity(intent)
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

            }




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


}







