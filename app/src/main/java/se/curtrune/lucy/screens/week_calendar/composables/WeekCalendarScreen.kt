package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.navigation.DayCalendarNavKey
import se.curtrune.lucy.screens.week_calendar.WeekChannel
import se.curtrune.lucy.screens.week_calendar.WeekEvent
import se.curtrune.lucy.screens.week_calendar.WeekViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekCalendarScreen(onPagerChange: (Int) -> Unit, navigate: (NavKey) -> Unit){
    //enableEdgeToEdge()
    val weekViewModel: WeekViewModel = viewModel()
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
                    navigate(DayCalendarNavKey(event.calendarDate.date.toString()))
                }
                is WeekChannel.ShowAddAllWeekNote -> {
                    println("show add to all week dialog")
                    showAddToAllWeekDialog = true
                }
                is WeekChannel.Navigate -> {
                    println("navigate to ${event.fragment.javaClass.name}")
                }
/*                    navigate(event.fragment)}
                WeekChannel.NavigateMyDay ->{}*/
                WeekChannel.NavigateMyDay -> {
                    println("navigate to my day")
                }
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = {
        weekViewModel.pagerState.numPages //pagerstate is a bad name, change it
    }, initialPage = weekViewModel.pagerState.initialPage
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                        })
                }, onEvent = { event ->
                    println("onEvent $event")
                    //devViewModel.onEvent(event)
                }
            )
        }

    ) { innerPadding ->
        HorizontalPager(state = pagerState ) {
            println(" pagerState current page ${pagerState.currentPage}")
            if (!pagerState.isScrollInProgress) {
                weekViewModel.onEvent(WeekEvent.OnPage(pagerState.currentPage))
            }
            WeekCalendar(
                modifier = Modifier.padding(innerPadding),
                state = state.value,
                onEvent = weekViewModel::onEvent
            )
        }
    }
    if( showAddItemDialog){
        AddItemBottomSheet(defaultItemSettings = weekViewModel.defaultItemSettings, onDismiss = {}) {
            showAddItemDialog = false
            weekViewModel.onEvent(WeekEvent.AddItem(it))
        }

    }

}