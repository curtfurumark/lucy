package se.curtrune.lucy.activities.kotlin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.viewmodel.WeekViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.ArrayList
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthCalendar(calendarDates: List<CalenderDate>, yearMonth: YearMonth, onDateClick: (CalenderDate)->Unit){
    println("MonthCalendar ...number of dates ${calendarDates.size}")
    val pagerState = rememberPagerState(pageCount = {
        10
    }, initialPage = 5)
/*    var monthHeader by remember{
        mutableStateOf("${LocalDate.now().month} ${LocalDate.now().year}")
    }*/


    Column() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = yearMonth.toString(), color = Color.White)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            DayOfWeek.values().iterator().forEach {
                //Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = it.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                    color = Color.White
                )
            }
        }
        println("...pagerState ${pagerState.currentPage}")
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), horizontalArrangement = Arrangement.Center,
            content = {
                if (calendarDates != null) {
                    items(calendarDates.size) { i ->
                        DateView(calendarDates[i], onCalendarDateClick = {
                            onDateClick(calendarDates[i])
                        })
                    }
                }
            }
        )
    }
}
/*@Composable
fun DateView(text: String){
    Box(modifier = Modifier
        .padding(2.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(5.dp))
        .background(Color.Blue),
        contentAlignment = Alignment.Center){
            Text(text = text)
    }
}*/
@Preview
@Composable
fun Preview(){
    //DateView(text = "hello")
}