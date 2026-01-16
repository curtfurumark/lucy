package se.curtrune.lucy.screens.monthcalendar.composables

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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState
import se.curtrune.lucy.screens.navigation.DayCalendarNavKey
import se.curtrune.lucy.screens.monthcalendar.MonthCalendarEvent
import se.curtrune.lucy.screens.monthcalendar.MonthChannel
import se.curtrune.lucy.screens.monthcalendar.MonthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthCalendarScreen(navigate: (NavKey) -> Unit, modifier: Modifier = Modifier){
    val viewModel  = viewModel<MonthViewModel>()
    val state = viewModel.state.collectAsState()
    var showAddItemDialog by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        pageCount = {
            viewModel.pagerState.numPages //pagerstate is a bad name, change it
        },
        initialPage = viewModel.pagerState.initialPage
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.collectAsState().value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                        }
                    )
                },
                onEvent = {
                    println("onEvent $it")
                }
            )
        }
    ) {padding->
        HorizontalPager(state = pagerState) {
            println(" pagerState current page ${pagerState.currentPage}")
            if (!pagerState.isScrollInProgress) {
                viewModel.onEvent(MonthCalendarEvent.Pager(pagerState.currentPage))
            }
            MonthCalendar(
                modifier = Modifier.padding(padding),
                state = state.value,
                onEvent = viewModel::onEvent
            )
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.eventChannel.collect{ event->
                when(event){
                    is MonthChannel.NavigateToDayCalendar -> {
                        navigate(DayCalendarNavKey(event.date.toString()))
                    }
                    is MonthChannel.ShowAddItemDialog -> {
                        showAddItemDialog = true
                    }
                    is MonthChannel.ShowMessage -> {
                        println("ShowMessage ${event.message}")
                    }
                }
            }
        }
    if( showAddItemDialog){
        AddItemBottomSheet(
            defaultItemSettings = viewModel.defaultItemSettings,
            onDismiss = {
                showAddItemDialog = false
            },
            onSave = {
                viewModel.onEvent(MonthCalendarEvent.InsertItem(it))
                showAddItemDialog = false
            }
        )
    }
}