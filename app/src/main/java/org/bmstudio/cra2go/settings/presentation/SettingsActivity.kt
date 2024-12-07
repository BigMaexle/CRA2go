package org.bmstudio.cra2go.settings.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.bmstudio.cra2go.R
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRA2goTheme {
                SettingsScreen(
                    onNavigate = { destination ->
                        when (destination) {
                            "General Settings" -> startActivity(Intent(this, GeneralSettingsActivity::class.java))
                            "Profile" -> startActivity(Intent(this, ProfileActivity::class.java))
                            "About" -> startActivity(Intent(this, AboutActivity::class.java))
                        }
                    },
                    onBack = { finish() } // Callback to finish the activity
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigate: (String) -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { (onBack()) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }},
                title = { Text("Settings") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                SettingsItem(
                    title = "General Settings",
                    iconRes = Icons.Default.Settings,
                    onClick = { onNavigate("General Settings") }
                )
            }
            item {
                SettingsItem(
                    title = "Profile",
                    iconRes = Icons.Default.AccountCircle,
                    onClick = { onNavigate("Profile") }
                )
            }
            item {
                SettingsItem(
                    title = "About",
                    iconRes = Icons.Default.Info,
                    onClick = { onNavigate("About") }
                )
            }
        }
    }
}



@Preview (showBackground = true)
@Composable
fun SettingsScreenPreview() {
    CRA2goTheme {
    SettingsScreen(
        onNavigate = { destination ->
            when (destination) {
                "General Settings" -> {}
                "Profile" -> {}
                "About" -> {}
            }
        },
        onBack = {}
    )}
}