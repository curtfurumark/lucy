package se.curtrune.lucy.screens.monthcalendar.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.screens.monthcalendar.MonthCalendarEvent
import java.time.LocalDate

@Composable
fun MonthDate(calendarDate:CalenderDate, onEvent: (MonthCalendarEvent)->Unit){
    val borderColor  = if( calendarDate.date == LocalDate.now() )Color.Yellow else Color.Gray
    Box(
        modifier = Modifier
            .border(BorderStroke(Dp.Hairline, borderColor))
            .aspectRatio(0.5f)
            //.clip(RoundedCornerShape(5.dp))
            .clickable {
                onEvent(MonthCalendarEvent.CalendarDateClick(calendarDate))
            },
        contentAlignment = Alignment.TopStart

    ){
        Column( horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                text = calendarDate.date.dayOfMonth.toString(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                fontSize = 14.sp)
            for(event in calendarDate.items){
                CalendarEvent(event = event)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMonthDate(){
    val calendarDate = CalenderDate()
    calendarDate.date = LocalDate.now()
    calendarDate.items = listOf(
        Item("pizza"),
        Item("tvättstuga")
    ).toMutableList()
    LucyTheme {
        MonthDate(calendarDate = calendarDate) { }
    }
}