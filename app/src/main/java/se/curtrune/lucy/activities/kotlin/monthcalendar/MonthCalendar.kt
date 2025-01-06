package se.curtrune.lucy.activities.kotlin.monthcalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.activities.kotlin.DateView
import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthCalendar(state: MonthCalendarState, onEvent: (MonthCalendarEvent)->Unit){
/*    val pagerState = rememberPagerState(pageCount = {
        10
    }, initialPage = 5)*/

    Column() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = state.yearMonth.toString(), color = Color.White)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            DayOfWeek.entries.iterator().forEach {
                Text(
                    text = it.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                    color = Color.White
                )
            }
        }
       // println("...pagerState ${pagerState.currentPage}")
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), horizontalArrangement = Arrangement.Center,
            content = {
                val calendarDates = state.calendarDates
                items(calendarDates.size){index->
                     DateView(calendarDates[index], onEvent = onEvent)

                }
            }
        )
    }
}
