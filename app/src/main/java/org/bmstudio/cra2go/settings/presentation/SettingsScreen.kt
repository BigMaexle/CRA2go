package org.bmstudio.cra2go.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.BatteryUnknown
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.R
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventViewModel
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventsEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.ConfirmDelete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    dutyEventViewModel: DutyEventViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showCalenderSettingsCard by remember { mutableStateOf(false) }


    val TAG: String = "SettingsScreen"

    val scope = rememberCoroutineScope()

    ConfirmDelete(
        onConfirm = { dutyEventViewModel.viewModelScope.launch {
            dutyEventViewModel.onEvent(DutyEventsEvent.DeleteAllEvents)
            showDeleteDialog = false
        } },
        onDismiss = { showDeleteDialog = false },
        showDialog = showDeleteDialog
    )




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors()
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {

            SettingsClickableComp(
                name = R.string.drawable_events,
                icon = Icons.Default.CalendarMonth,
                iconDesc = R.string.drawable_events,
            ) {
                showCalenderSettingsCard = true
            }
            SettingsClickableComp(
                name = R.string.deleteall,
                icon = Icons.Default.Delete,
                iconDesc = R.string.deleteall,
            ) {
                showDeleteDialog = true
            }

            /*SettingsTextComp(
                icon = Icons.Default.AirplanemodeActive,
                iconDesc = R.string.drawable_events,
                name = R.string.Airline,
                state = viewModel.getAirline(),
                onSave = {viewModel.saveAirline(it) },
                onCheck = {viewModel.checkTextInput(it)})*/


        }

        if (showCalenderSettingsCard){

            val optionnames = stringArrayResource(id = R.array.eventFilter)

            val current_filter = viewModel.getEventFilters()

            CalenderSettingsCard(
                title = R.string.eventFiltertitle,
                currentFilter = current_filter,
                options = optionnames,
                onAccept = { new_filter ->
                    showCalenderSettingsCard = false
                    viewModel.saveCalenderFilter(new_filter)}
            )
        }
    }





}
