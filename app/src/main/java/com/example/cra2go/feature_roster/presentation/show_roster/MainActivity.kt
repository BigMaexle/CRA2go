package com.example.cra2go.feature_roster.presentation.show_roster

import android.content.Context
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.cra2go.ui.theme.CRA2goTheme
import dagger.hilt.android.AndroidEntryPoint
import java.security.AccessController.getContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "Main"


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




