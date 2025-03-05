package se.curtrune.lucy.screens.monthcalendar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.monthcalendar.MonthCalendarEvent
import se.curtrune.lucy.screens.monthcalendar.MonthCalendarState
import se.curtrune.lucy.util.DateTImeFormatter
import se.curtrune.lucy.util.cecilia

@Composable
fun MonthCalendar(state: MonthCalendarState, onEvent: (MonthCalendarEvent)->Unit){
    val ym = state.yearMonth
    println("MonthCalendar()year: ${ym.year} month: ${ym.month} ")
    Column(
        modifier = Modifier.background(Color.Black)) {
        MonthHeader(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(4.dp))
        WeekDaysHeader()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.Center,
            content = {
                val calendarDates = state.calendarMonth!!.calenderDates
                items(calendarDates.size){index->
                     MonthDate(calendarDates[index], onEvent = onEvent)
                }
            }
        )
    }
}

@Composable
fun MonthHeader(state: MonthCalendarState, onEvent: (MonthCalendarEvent) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
        .clickable {
            onEvent(MonthCalendarEvent.MonthYear(state.yearMonth))
        },
        horizontalArrangement = Arrangement.Center) {
        Text(text = DateTImeFormatter.format(state.yearMonth).cecilia(), fontSize = 18.sp)
    }
}
