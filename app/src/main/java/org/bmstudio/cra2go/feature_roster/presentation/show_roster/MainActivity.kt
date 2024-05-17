package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint
import org.bmstudio.cra2go.ui.theme.CRA2goTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            CRA2goTheme {

                DutyEventScreen()

            }


        }
    }

}




