package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun DutyScreenDropDown (
    forceLogin: ForceLogin,
    menustate: MutableState<Boolean>
) {

    val scope = rememberCoroutineScope()

    var switchState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        switchState = forceLogin.get()
    }

    DropdownMenu(expanded = menustate.value,
        onDismissRequest = { scope.launch {
            menustate.value = false
        }})
    {
        DropdownMenuItem(
            onClick = { /* Handle click if needed */ },
            text = { Text(text = "Force Login")},
            trailingIcon = {
                Checkbox(
                    checked = switchState,
                    onCheckedChange = {
                        switchState = it
                        scope.launch {
                            forceLogin.switch()
                        }
                    }
                )
            })
    }
}