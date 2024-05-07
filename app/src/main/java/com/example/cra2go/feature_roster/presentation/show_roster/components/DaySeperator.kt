package com.example.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Dayseparator (day: Date){
    
    Box(modifier = Modifier
        .height(30.dp)
        .fillMaxWidth()
    )    {
    
    val sdf = SimpleDateFormat("EE dd.MM.yy")
    
    Divider()
    Text(text = sdf.format(day), modifier = Modifier.padding(horizontal = 10.dp,vertical = 5.dp))
    Divider()

    }}

@Preview (showBackground = true)
@Composable
fun showToday() {
    Dayseparator(day = Date())
}