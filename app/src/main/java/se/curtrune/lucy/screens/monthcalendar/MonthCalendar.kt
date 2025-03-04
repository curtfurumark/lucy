package se.curtrune.lucy.screens.monthcalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.util.DateTImeFormatter
import se.curtrune.lucy.util.cecilia

@Composable
fun MonthCalendar(state: MonthCalendarState, onEvent: (MonthCalendarEvent)->Unit){
    val ym = state.yearMonth
    println("MonthCalendar()year: ${ym.year} month: ${ym.month} ")
    Column(modifier = Modifier.background(Color.Black)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = DateTImeFormatter.format(state.yearMonth).cecilia(), fontSize = 18.sp)
        }
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
