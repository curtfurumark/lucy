package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.week_calendar.WeekEvent
import se.curtrune.lucy.screens.week_calendar.WeekState

@Composable
fun WeekCalendar(modifier: Modifier = Modifier, state: WeekState, onEvent: (WeekEvent) -> Unit){
    Column(modifier = modifier){
        CalendarWeekHeading(week = state.currentWeek)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            item{ ThisWeekView(state = state, onEvent = onEvent) }
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[3], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[0], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[4], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[1], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[5], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[2], onEvent = onEvent)}
            item{ WeekDate(calendarDate = state.calendarWeek.calendarDates[6], onEvent = onEvent)}
        }
    }
}

@Composable
fun ThisWeekView(state: WeekState, onEvent: (WeekEvent) -> Unit){
    println("ThisWeekView, number of items: ${state.calendarWeek.allWeekItems.size}")
    Box(
        modifier = Modifier
            .border(2.dp, color = Color.DarkGray)
            .aspectRatio(1.2F)
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .clickable {
                onEvent(WeekEvent.OnAllWeekClick(state.currentWeek))
            },
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.week_with_number, state.currentWeek.weekNumber),
                fontSize = 20.sp
            )
            state.calendarWeek.allWeekItems.forEach{ item ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(Dp.Hairline, color = Color.Black)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = item.heading,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

