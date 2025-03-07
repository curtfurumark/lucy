package se.curtrune.lucy.screens.user_settings.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.google_calendar.GoogleCalendarEvent
import se.curtrune.lucy.screens.user_settings.UserState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarEventsList(state: UserState){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.calendarEvents){event->
            CalendarEvent(event = event)
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarEvent(event: GoogleCalendarEvent){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "event id: ${event.eventID}")
        Text(text = "calendar id: ${event.calendarID}")
        Text(text = "title: ${event.title}")
        Text(text = "location: ${event.location}")
        Text(text = "account name: ${event.accountName}")
        Text(text = "start time: ${formatMillisEpoch( event.dtStart)}")
        Text(text = "end time: ${formatMillisEpoch( event.dtEnd)}")
        Text(text = "is all day: ${event.isAllDay}")

    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatMillisEpoch(millis: Long): String{
    return  LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()).toString()
}