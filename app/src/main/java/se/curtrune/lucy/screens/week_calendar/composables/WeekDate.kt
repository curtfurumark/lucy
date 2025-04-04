package se.curtrune.lucy.screens.week_calendar.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.screens.week_calendar.WeekEvent
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalDate
import java.time.format.TextStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeekDate(calendarDate: CalenderDate, onEvent: (WeekEvent)->Unit){
    var backgroundColor by remember {
        mutableStateOf(Color.Transparent)
    }
    var borderColor by remember {
        mutableStateOf(Color.DarkGray)
    }
    if( calendarDate.date == LocalDate.now()){
        println("is today")
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        borderColor = MaterialTheme.colorScheme.onSecondaryContainer
    }else{ //shouldn't be necessary, but it is,
        borderColor = Color.Transparent
        borderColor= Color.DarkGray
    }
    Box(
        modifier = Modifier
            .border(2.dp, color = borderColor)
            .aspectRatio(1.2F)
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .combinedClickable(onClick = {
                onEvent(WeekEvent.CalendarDateClick(calendarDate)) },
                onLongClick = {
                    onEvent(WeekEvent.CalendarDateLongClick(calendarDate))
                }),
            contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                text = "${DateTImeConverter.format(calendarDate.date.dayOfWeek, TextStyle.SHORT)} ${calendarDate.date.dayOfMonth}",
                fontSize = 20.sp)
            calendarDate.items.forEach { event->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(Dp.Hairline, color = Color.Black)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 2.dp),
                        text = "${DateTImeConverter.format(event.targetTime)} ${event.heading}",
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
@Preview( uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewWeekDate(){
    LucyTheme {
        val calendarDate = CalenderDate()
        calendarDate.date = LocalDate.now()
        calendarDate.items = listOf(
            Item("dev"),
            Item("play bass")
        )
        WeekDate(calendarDate = calendarDate) {
            println("hello")
        }
    }
}