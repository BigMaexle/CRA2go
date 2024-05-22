package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun Dayseparator(day: Date) {

    val today = Calendar.getInstance()

    val day_color = if (areSameDay(today,day)) {
        Color.LightGray
    }else{
        Color.LightGray
    }

    Box(
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth()
            .background(day_color)
    ) {

        val sdf = SimpleDateFormat("EE dd.MM.yy")

        Text(
            text = sdf.format(day),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
        Divider()

    }
}

fun areSameDay(cal: Calendar, date: Date): Boolean {
    val calFromDate = Calendar.getInstance()
    calFromDate.time = date

    return cal.get(Calendar.YEAR) == calFromDate.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == calFromDate.get(Calendar.MONTH) &&
            cal.get(Calendar.DAY_OF_MONTH) == calFromDate.get(Calendar.DAY_OF_MONTH)
}
@Preview(showBackground = true)
@Composable
fun showToday() {
    Dayseparator(day = Date())
}