package se.curtrune.lucy.screens.week_calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.week_calendar.composables.CalendarWeekHeading
import se.curtrune.lucy.screens.week_calendar.composables.WeekCalendar
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WeekFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val weekViewModel = viewModel<WeekViewModel>()
                val state = weekViewModel.state.collectAsState()
                var showAddItemDialog by remember {
                    mutableStateOf(false)
                }
                var showMessageDialog by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(weekViewModel) {
                    weekViewModel.eventFlow.collect{ event->
                        when(event){
                            is WeekChannel.ShowAddItemDialog -> {
                                showAddItemDialog = true
                            }
                            is WeekChannel.ShowMessage -> {
                                showMessageDialog = true
                            }
                            is WeekChannel.ViewDay -> {
                                navigate(event.calendarDate)
                            }
                        }
                    }
                }
                LucyTheme {
                    Scaffold(){ padding ->
                        val pagerState = rememberPagerState(pageCount = {
                            weekViewModel.pagerState.numPages //pagerstate is a bad name, change it
                        }, initialPage = weekViewModel.pagerState.initialPage
                        )
                        HorizontalPager(state = pagerState) {
                            println(" pager state ${pagerState.currentPage}")
                            if (!pagerState.isScrollInProgress) {
                                weekViewModel.onEvent(WeekEvent.OnPage(pagerState.currentPage))
                            }
                            WeekCalendar(
                                modifier = Modifier.padding(padding),
                                state = state.value,
                                onEvent = { event ->
                                    weekViewModel.onEvent(event)
                                })
                        }
                    }
                    if(showAddItemDialog){
                        //val settings = I
                        AddItemDialog(onDismiss = {
                            showAddItemDialog = false
                        }, onConfirm = { item->
                            weekViewModel.onEvent(WeekEvent.AddItem(item))
                            showAddItemDialog = false
                        }, settings = weekViewModel.dialogSettings)
                    }
                    if(showMessageDialog){


                    }
                }
            }
        }
    }


    private fun navigate(calendarDate: CalenderDate){
        println("MonthFragment.navigate(CalendarDate")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            CalendarDayFragment(
                calendarDate
            )
        )
    }

    @Composable
    @Preview
    fun MyPreview(){
        Column {
            CalendarWeekHeading(Week())
            WeekFragment()
        }
    }

}

