package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.screens.daycalendar.DateEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayOfWeek(date: LocalDate, state: DayCalendarState, onEvent: (DateEvent) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onEvent(DateEvent.CurrentDate(date)) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (date == state.date) Color.White else Color.Gray
        Text(
            text =
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                .uppercase(),
            color = color
        )
        Text(text = date.dayOfMonth.toString(), color = color)
    }
}

@Composable
fun DaysOfWeek(state: DayCalendarState, onEvent: (DateEvent)->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        var date = Week(state.date).firstDateOfWeek
        for(i in 1..7){
            Column(modifier = Modifier
                .weight(1f)
                .border(BorderStroke(Dp.Hairline, color = Color.DarkGray)),
                horizontalAlignment = Alignment.CenterHorizontally) {
                DayOfWeek(date,state,  onEvent = onEvent)
                date = date.plusDays(1)
            }
        }
    }
}
