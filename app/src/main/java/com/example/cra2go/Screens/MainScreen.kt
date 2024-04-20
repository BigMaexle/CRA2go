package com.example.cra2go.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.cra2go.classes.LHFlightRequest
import com.example.cra2go.classes.ShowSampleFlight
import com.example.cra2go.ui.theme.CRA2goTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMainScreen(queue : RequestQueue){

}

@Composable
@Preview
fun ShowtestPopup() {
    Popup {
        Text(text = "Test")
    }
}

