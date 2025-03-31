package se.curtrune.lucy.screens.dev.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestGoogleCalendars(){
    var calendarID by remember {
        mutableStateOf("7")
    }
    var firstDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val calendarModule = LucindaApplication.appModule.googleCalendarModule
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "just testing calendar module", fontSize = 20.sp)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = calendarID,
            onValueChange = {
                calendarID = it
            }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstDate.toString(),
            onValueChange = {
                firstDate = LocalDate.parse(it)
                }
            )
        Row(modifier = Modifier.fillMaxWidth()) {

            Button(onClick = {
                val items = calendarModule.getGoogleEventsAsItems(calendarID.toInt())
                items.forEach { item ->
                    println(item.toString())
                }
            }) {
                Text(text = "get items")
            }
            Button(onClick = {
                val items = calendarModule.getEvents(calendarID.toInt(), firstDate)
                items.forEach { googleEvent ->
                    println(googleEvent.toString())
                }
            }) {
                Text(text = "get items from date")
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                val calendars = calendarModule.getCalendars()
                calendars.forEach { calendar ->
                    println(calendar.toString())
                }
            }) {
                Text(text = "get calendars")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun PreviewTestGoogleCalendars(){
    LucyTheme {
        TestGoogleCalendars()
    }
}