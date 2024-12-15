package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.EventDetailCard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

class EventDetailCardActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRA2goTheme{

            }
        }
    }
}