package se.curtrune.lucy.screens.monthcalendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.LocalDate

@Composable
fun MonthDate(calendarDate:CalenderDate, onEvent: (MonthCalendarEvent)->Unit){
    Box(
        modifier = Modifier
            .border(BorderStroke(Dp.Hairline, Color.Gray))
            .aspectRatio(0.5f)
            //.clip(RoundedCornerShape(5.dp))
            .clickable {
                onEvent(MonthCalendarEvent.CalendarDateClick(calendarDate))
            },
        contentAlignment = Alignment.TopStart

    ){
        Column( horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = calendarDate.date.dayOfMonth.toString(), fontSize = 14.sp, color = Color.White)
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
    calendarDate.items = listOf(Item("pizza"), Item("tvättstuga"))
    LucyTheme {
        MonthDate(calendarDate = calendarDate) { }
    }
}