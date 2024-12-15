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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.LocalDate
import java.time.YearMonth
import java.util.ArrayList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthCalendar(calendarDates: List<CalenderDate>){
    println("MonthCalendar ...number of dates ${calendarDates.size}")
    val pagerState = rememberPagerState(pageCount = {
        10
    }, initialPage = 5)
    //val header = "${LocalDate.now().month} ${LocalDate.now().year}"
    var monthHeader by remember{
        mutableStateOf("${LocalDate.now().month} ${LocalDate.now().year}")
    }
/*    var monthYear by remember{
        mutableStateOf(MonthYear.now())
    }*/

    Column() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = monthHeader)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "m")
            Text(text = "t")
            Text(text = "w")
            Text(text = "t")
            Text(text = "f")
            Text(text = "s")
            Text(text = "s")
        }
        HorizontalPager(state = pagerState) {
            println("...pagerState ${pagerState.currentPage}")
            monthHeader =
                "${YearMonth.now().plusMonths((pagerState.currentPage - 5).toLong())}"
            LazyVerticalGrid(
                columns = GridCells.Fixed(7), horizontalArrangement = Arrangement.Center,
                content = {
                    if (calendarDates != null) {
                        items(calendarDates.size) { i ->
                            DateView(calendarDates[i].date, calendarDates[i].items)
                        }
                    }
                }
            )
        }
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