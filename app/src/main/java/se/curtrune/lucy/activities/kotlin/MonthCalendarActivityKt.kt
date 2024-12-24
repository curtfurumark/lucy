package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.LucindaTopAppBar
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.viewmodel.CalendarMonthViewModel
import se.curtrune.lucy.viewmodel.MonthViewModel
import se.curtrune.lucy.viewmodel.WeekViewModel
import java.time.YearMonth

class MonthCalendarActivityKt : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = { LucindaTopAppBar()}) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val monthViewModel = viewModel<MonthViewModel>(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return MonthViewModel(applicationContext) as T
                                }
                            }
                        )
                        var calendarDates by remember{
                            mutableStateOf(monthViewModel.mutableCalendarDates)
                        }
                        var yearMonth by remember{
                            mutableStateOf(monthViewModel.yearMonth)
                        }
                        val pagerState = rememberPagerState(pageCount = {
                            10
                        }, initialPage = 5)
                        HorizontalPager(state =pagerState ) {
                            println(" pager state ${pagerState.currentPage}")
                            monthViewModel.onPager(pagerState.currentPage)
                            calendarDates.value?.let { yearMonth.value?.let { it1 ->
                                MonthCalendar(it,
                                    it1,
                                    onDateClick = {
                                        println(" onCalendarDate click")
                                    }
                                )
                            } }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun AddItemFloatingActionButton(){
    FloatingActionButton(onClick = { /*TODO*/ }) {
/*        Icon(imageVector = Icons.Default.Add, modifier =  Modifier, color = Color.Green){

        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    LucyTheme {
        //MonthCalendar()
    }
}