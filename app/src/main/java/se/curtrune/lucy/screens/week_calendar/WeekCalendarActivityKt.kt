package se.curtrune.lucy.screens.week_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.viewmodel.WeekViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekCalendarActivityKt : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                EnergyBox()
                MyWeekCalendar()
            }
        }
    }

    @Composable
    fun CalendarWeekHeading(week: Week) {
        Row( modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = week.month.toString(),
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "v ${week.weekNumber}",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center)
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
    fun MyWeekCalendar(){
        val weekViewModel = viewModel<WeekViewModel>(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeekViewModel(applicationContext) as T
                }
            }
        )
        var calendarDates by remember {
            mutableStateOf(weekViewModel.calendarDates.value)
        }
        var currentPage by remember{
            mutableIntStateOf(weekViewModel.initialPage)
        }
        val pagerState = rememberPagerState(pageCount = {
            weekViewModel.numPages
        }, initialPage = currentPage)
        var currentWeek by remember {
            mutableStateOf(weekViewModel.mutableWeek)
        }
        weekViewModel.calendarDates.observe(LocalLifecycleOwner.current){
            println("calendarDates on changed ")
            calendarDates = it
        }
        currentWeek?.let { it.value?.let { it1 -> CalendarWeekHeading(week = it1) } }
        HorizontalPager(state = pagerState) {
            //println("...pagerState ${pagerState.currentPage} currentPage $currentPage, it$it")
            if( pagerState.currentPage != currentPage){
                println("...pagerState ${pagerState.currentPage} currentPage $currentPage, it$it")
                println("...hey ho lets go")
                weekViewModel.onPage(pagerState.currentPage)
                currentPage = pagerState.currentPage
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                calendarDates?.let { it1 ->
                    items(it1.size) {
                        val calendarDate = calendarDates?.get(it)
                        if (calendarDate != null) {
                            DateView(calendarDate, onClick = {
                                println(" on calendar date click ${it.date}")
                            })
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun DateView(calendarDate: CalenderDate, onClick: (CalenderDate)->Unit){
        Box(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Blue)
                .clickable {
                    onClick(calendarDate)
                },
            contentAlignment = Alignment.TopStart
        ){
            Column( modifier = Modifier.padding(4.dp), horizontalAlignment =Alignment.Start ) {
                Text(text = formatDate(calendarDate.date), fontSize = 20.sp, color = Color.White)
                for(event in calendarDate.items){
                    Text(text = "${event.targetTime.toString()} ${event.heading}", color = Color.Yellow)
                }
            }
        }
    }
    private fun formatDate(date: LocalDate): String{
        return date.format(DateTimeFormatter.ofPattern("d E"))
        //String.format(Locale.getDefault(), "%d %s", date.dayOfMonth, date.dayOfWeek.getDisplayName())
    }
    @Composable
    @Preview
    fun MyPreview(){
        Column {
            CalendarWeekHeading(Week())
            WeekCalendarActivityKt()
        }
    }

}

