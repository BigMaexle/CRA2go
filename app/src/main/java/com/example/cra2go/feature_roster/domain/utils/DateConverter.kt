package com.example.cra2go.feature_roster.domain.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cra2go.ui.theme.CRA2goTheme
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


object DateConverter {
    fun convertfromDateStamp(dateString: String): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd'Z'")
        return try {
            formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null // Return null in case of parsing failure
        }
    }

    fun convertToTimestamp(dateString: String): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return try {
            formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null // Return null in case of parsing failure
        }
    }


}

@Preview(showBackground = true)
@Composable
fun testdate(){

    val td = "2024-04-19T18:00:00Z"
    val formatter = SimpleDateFormat("HH:mm")
    val text = DateConverter.convertToTimestamp(td)?.let { formatter.format(it) }


    CRA2goTheme {

        if (text != null) {
            Text(text = text)
        }
    }
}

