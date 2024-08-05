package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import android.app.BroadcastOptions
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReadMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.compose.NavHost
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.domain.model.LoginActivityContract
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.DutyScreenDropDown
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.ForceLogin
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.MenuItems
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

    val ctx = LocalContext.current

    val ListScrollState = rememberLazyListState()

    val state = viewModel.state.value

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val title = if (BuildConfig.DEBUG) {"CRA2go - MOCK"} else {"CRA2go"}

    var menustate = remember { mutableStateOf(false) }

    var forceLogin = remember { ForceLogin(ctx) }

    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = { MenuItems(forceLogin,drawerState) },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = title,modifier = Modifier.fillMaxWidth(1f),
                        textAlign = TextAlign.Center) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {menustate.value = !menustate.value} ) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "more" )
                            DutyScreenDropDown(
                                forceLogin = forceLogin,
                                menustate = menustate)
                        }
                    }
                )
            },
            bottomBar = { 
                BottomDutyPlanBar(
                    DutyListState = ListScrollState
                )
            }
        ) { innerPadding ->

            verticalCalender(
                listState = ListScrollState,
                events = state.events,
                padding = innerPadding)


        }


    }

}

@Composable
fun BottomDutyPlanBar(
    DutyListState: LazyListState,
    viewModel: DutyEventViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    
    NavigationBar {
//        NavigationBarItem(
//            selected = false,
//            onClick = {},
//            icon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Plan")}
//        )
        NavigationBarItem(
            selected = true,
            onClick = {
                scope.launch {
                    val today = Calendar.getInstance()
                    DutyListState.animateScrollToItem(today.get(Calendar.DAY_OF_MONTH)-1)}
            },
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "List")}
        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /*TODO*/ },
//            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "List")}
//        )
    }
    
}


@Preview
@Composable
fun DutyEventScreen_preview(){
    CRA2goTheme {
        DutyEventScreen()
    }
}


