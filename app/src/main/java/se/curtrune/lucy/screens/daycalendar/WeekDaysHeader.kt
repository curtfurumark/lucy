package se.curtrune.lucy.screens.daycalendar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.calender.Week
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayOfWeek(date: LocalDate, onEvent: (DateEvent) -> Unit){
    Column (
        modifier = Modifier
            .border(Dp.Hairline, Color.Gray)
            //.weight(1F, fill = true)
            .clickable { onEvent(DateEvent.CurrentDate(date)) },
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()).uppercase())
        Text(text = date.dayOfMonth.toString())
    }
}

@Composable
fun DaysOfWeek(week: Week, onEvent: (DateEvent)->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        var date = week.firstDateOfWeek;
        for(i in 1..7){
            DayOfWeek(date, onEvent = onEvent)
            date = date.plusDays(1)
        }
    }
}

@Composable
@Preview
fun Preview(){
    DaysOfWeek(week = Week(), onEvent = {})

}