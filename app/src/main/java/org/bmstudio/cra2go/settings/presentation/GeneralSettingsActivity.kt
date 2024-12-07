package org.bmstudio.cra2go.settings.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

class GeneralSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRA2goTheme{
                GeneralSettingsScreen(onBack = { finish() })
            }
        }
    }
}