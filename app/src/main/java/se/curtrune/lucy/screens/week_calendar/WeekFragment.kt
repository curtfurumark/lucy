package se.curtrune.lucy.screens.week_calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.week_calendar.composables.CalendarWeekHeading
import se.curtrune.lucy.screens.week_calendar.composables.AddAllWeekItemDialog
import se.curtrune.lucy.screens.week_calendar.composables.WeekCalendar

class WeekFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val weekViewModel = viewModel<WeekViewModel>()
                val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
                mainViewModel.setTitle("vecka")
                val state = weekViewModel.state.collectAsState()
                var showAddItemDialog by remember {
                    mutableStateOf(false)
                }
                var showMessageDialog by remember {
                    mutableStateOf(false)
                }
                var showAddToAllWeekDialog by remember {
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
                            is WeekChannel.ShowAddAllWeekNote -> {
                                println("show add to all week dialog")
                                showAddToAllWeekDialog = true
                            }
                            is WeekChannel.Navigate -> {navigate(event.fragment)}
                            WeekChannel.NavigateMyDay ->{}
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
/*                    if(showAddItemDialog){
                        //val settings = I
                        AddItemDialog(onDismiss = {
                            showAddItemDialog = false
                        }, onConfirm = { item->
                            weekViewModel.onEvent(WeekEvent.AddItem(item))
                            showAddItemDialog = false
                        }, settings = weekViewModel.dialogSettings)
                    }*/
                    if(showMessageDialog){
                        println("show message dialog")
                    }
                    if(showAddToAllWeekDialog){
                        println("show add to all week dialog")
                        AddAllWeekItemDialog(onDismiss = {
                            showAddToAllWeekDialog = false
                        },
                            onConfirm = { item->
                                weekViewModel.onEvent(WeekEvent.AddAllWeekItem(item))
                                showAddToAllWeekDialog = false
                            })
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
    private fun navigate(fragment: Fragment){
        println("navigate to ${fragment.javaClass.name}")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(fragment)
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

