package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import org.bmstudio.cra2go.ui.theme.Pink40
import org.bmstudio.cra2go.ui.theme.Purple40
import org.bmstudio.cra2go.ui.theme.Purple80
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun Dayseparator(day: Date) {

    val today = Calendar.getInstance()

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {

        Row (
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            val sdf = SimpleDateFormat("EE dd.MM.yy")

            val date_background: Color = day_background_color(today,day)


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(0.dp)
                    .background(date_background, RoundedCornerShape(100))
                )
            {

                Text(
                    text = sdf.format(day),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),

                )
            }

            Box(modifier = Modifier.width(5.dp))


            Column (
                verticalArrangement = Arrangement.Center
            ){
                Spacer(modifier = Modifier.weight(1f))
                Divider()
                Spacer(modifier = Modifier.weight(1f))
            }
        }



    }
}

@Composable
fun day_background_color(today: Calendar, day: Date): Color {

    val c = Calendar.getInstance()
    c.time = day

    var color: Color = MaterialTheme.colorScheme.background

    if (areSameDay(today,day)){
        color = MaterialTheme.colorScheme.secondaryContainer
        return color
    }
    //if (c.get(Calendar.DAY_OF_WEEK) == 7 || c.get(Calendar.DAY_OF_WEEK) == 1) {color = Purple80}

    return color

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
fun ShowToday() {
    Dayseparator(day = Date())
}