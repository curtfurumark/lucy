package se.curtrune.lucy.activities.kotlin.monthcalendar

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.LucindaTopAppBar
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Mental

class MonthCalendarActivityKt : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = { LucindaTopAppBar(Mental(), onEvent = {
                    println("mood event $it")
                })}) {
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
                        val state = monthViewModel.state.collectAsState()
                        val pagerState = rememberPagerState(pageCount = {
                            10
                        }, initialPage = 5)
                        HorizontalPager(state =pagerState ) {
                            println(" pager state ${pagerState.currentPage}")
                            monthViewModel.onPager(pagerState.currentPage)
                            MonthCalendar(state = state.value, onEvent = { event->
                                println("month calendar event $event ")
                            })
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    LucyTheme {
        //MonthCalendar()
    }
}