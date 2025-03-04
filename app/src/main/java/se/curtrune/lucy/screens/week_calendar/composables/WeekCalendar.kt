package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.week_calendar.WeekEvent
import se.curtrune.lucy.screens.week_calendar.WeekState

@Composable
fun WeekCalendar(modifier: Modifier = Modifier, state: WeekState, onEvent: (WeekEvent) -> Unit){
    Column(modifier = modifier){
        CalendarWeekHeading(week = state.currentWeek)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(state.calendarDates) { calendarDate ->
                WeekDate(calendarDate = calendarDate, onEvent = onEvent)
            }
        }
    }
}