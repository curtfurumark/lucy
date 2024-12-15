package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.workers.CalenderWorker
import java.time.LocalDate

class WeekCalendarActivityKt : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                EnergyBox()
                CalendarWeekHeading(LocalDate.now())
                val calendarDates = CalenderWorker.getEvents( Week(), applicationContext)
                MyWeekCalendar(calendarDates)
            }
        }
    }

    @Composable
    fun CalendarWeekHeading(date: LocalDate) {
        val weekNumber = CalenderWorker.getWeekNumber(date);
        Row( verticalAlignment =  Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(16.dp)) {
            Text(text = date.month.toString(),
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "   v $weekNumber",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
    @Composable
    fun EnergyBox(){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Blue)
            .padding(16.dp)
        ){
            Text(text = "energi", fontSize = 24.sp, color = Color.White)
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MyWeekCalendar(calendarDates: List<CalenderDate>){
        val pagerState = rememberPagerState(pageCount = {
            10
        }, initialPage = 5)
        HorizontalPager(state = pagerState) {
            println("...pagerState ${pagerState.currentPage}")
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    items(calendarDates.size) { i ->
                        DateView(date = calendarDates[i].date, events = calendarDates[i].items)
                    }
                }
            )
        }
    }
    @Composable
    fun DateView(date: LocalDate, events: List<Item>){
        Box(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Blue),
            contentAlignment = Alignment.TopStart
        ){
            Column( modifier = Modifier.padding(16.dp), horizontalAlignment =Alignment.CenterHorizontally ) {
                Text(text = date.toString(), fontSize = 20.sp, color = Color.White)
                for(event in events){
                    Text(text = "${event.targetTime.toString()} ${event.heading}", color = Color.Yellow)
                }
            }
        }
    }
    @Composable
    @Preview
    fun MyPreview(){
        Column {
            CalendarWeekHeading(LocalDate.now())
            WeekCalendarActivityKt()
        }
    }

}

