package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.screens.week_calendar.WeekEvent

@Composable
fun WeekDate(calendarDate: CalenderDate, onEvent: (WeekEvent)->Unit){
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Blue)
            .clickable {
                onEvent(WeekEvent.CalendarDateClick(calendarDate))
                //onClick(calendarDate)
            },
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = calendarDate.date.toString(),
                fontSize = 20.sp)
            for(event in calendarDate.items){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(Dp.Hairline, color = Color.Black)) {
                    Text(
                        text = "${event.targetTime.toString()} ${event.heading}",
                        maxLines = 1
                    )
                }
            }
        }
    }
}