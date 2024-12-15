package org.bmstudio.cra2go.settings.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.bmstudio.cra2go.R

@Composable
fun CalenderSettingsCard(
    @StringRes title: Int,
    currentFilter: List<Boolean>,
    options: Array<String>,
    onAccept: (List<Boolean>) -> Unit
) {

    val TAG: String = "CalenderSettingsCard"

    if (currentFilter.size != options.size){
        Log.e(TAG,"Option Names and Filter are not the same lenght, returning")
        Log.e(TAG,currentFilter.size.toString())
        Log.e(TAG,"Options Size:"+options.size.toString())
        return
    }

    // Track the state of each checkbox
    val checkboxStates = remember { mutableStateListOf(*Array(options.size) { it -> currentFilter[it]}) }


    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),

            ) {
            Column(modifier = Modifier.padding(25.dp)) {
                // Title Line
                Text(
                    text = stringResource(id = title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // List of Checkboxes with Options on the Left
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { checkboxStates[index] = !checkboxStates[index] }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Checkbox(
                            checked = checkboxStates[index],
                            onCheckedChange = { isChecked -> checkboxStates[index] = isChecked }
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.AppNeedsRestart),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall)

                // Accept Button in the lower right
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentAlignment = androidx.compose.ui.Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = { onAccept(checkboxStates) }) {
                        Text("Accept")
                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun SettingsCardPreview() {
    val options = arrayOf("Option 1", "Option 2", "Option 3")
    CalenderSettingsCard(title = R.string.eventFiltertitle, options = options, onAccept = {}, currentFilter = listOf(true,false,true))
}
