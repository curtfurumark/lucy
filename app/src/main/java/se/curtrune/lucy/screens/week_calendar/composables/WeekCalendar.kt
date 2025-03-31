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
import androidx.compose.foundation.lazy.grid.items
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
            items(state.calendarWeek.calendarDates){ calendarDate->
                WeekDate(calendarDate = calendarDate, onEvent = onEvent)
            }
        }
    }
}



