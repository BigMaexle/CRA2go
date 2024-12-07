package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender.CalendarView
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.DutyScreenDropDown
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.ViewState
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.ForceLogin
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.MenuItems
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.ViewStateSaver
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.verticalCalender
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
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

    val menustate = remember { mutableStateOf(false) }

    val forceLogin = remember { ForceLogin(ctx) }

    // Remember the current view state and save it across configuration changes
    var currentView by rememberSaveable(stateSaver = ViewStateSaver){ mutableStateOf<ViewState>(ViewState.ListViewState) }

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
                    currentView = currentView,
                    DutyListState = ListScrollState,
                    onItemClick = { newView ->
                        currentView = newView
                    }
                )
            }
        ) { innerPadding ->

            when (currentView) {
                is ViewState.CalenderViewState -> {
                    CalendarView(
                        events = state.events,
                        padding = innerPadding,
                    )
                }
                is ViewState.ListViewState -> {
                    verticalCalender(
                        listState = ListScrollState,
                        events = state.events,
                        padding = innerPadding)
                }
            }



        }


    }

}

@Composable
fun BottomDutyPlanBar(
    currentView: ViewState,
    DutyListState: LazyListState,
    onItemClick: (ViewState) -> Unit,
    viewModel: DutyEventViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    
    NavigationBar {
        NavigationBarItem(
            selected = currentView is ViewState.CalenderViewState,
            onClick = {
                onItemClick(ViewState.CalenderViewState)
            },
            icon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Plan")}
        )
        NavigationBarItem(
            selected = currentView is ViewState.ListViewState,
            onClick = {
                onItemClick(ViewState.ListViewState)
                scope.launch {
                    val today = Calendar.getInstance()
                    DutyListState.animateScrollToItem(today.get(Calendar.DAY_OF_YEAR)-1)}
            },
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "List")}
        )
    }
    
}


@Preview
@Composable
fun DutyEventScreen_preview(){
    CRA2goTheme {
        DutyEventScreen()
    }
}


