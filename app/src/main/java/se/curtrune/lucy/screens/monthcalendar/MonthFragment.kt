package se.curtrune.lucy.screens.monthcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.monthcalendar.composables.MonthCalendar

class MonthFragment : Fragment() {

    private fun navigate(calendarDate: CalenderDate){
        println("MonthFragment.navigate(CalendarDate)")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            CalendarDayFragment(
                calendarDate
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LucyTheme{
                    val monthViewModel = viewModel<MonthViewModel>()
                    val state = monthViewModel.state.collectAsState()
                    var showAddItemDialog by remember{
                        mutableStateOf(false)
                    }
                    var showMessage by remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(monthViewModel) {
                        monthViewModel.eventChannel.collect { event ->
                            when (event) {
                                MonthChannel.ShowAddItemDialog -> {
                                    showAddItemDialog = true
                                }
                                is MonthChannel.ShowMessage -> {
                                    showMessage = true
                                }

                                MonthChannel.NavigateToDayCalendar -> {
                                    state.value.currentCalendarDate?.let { navigate(it) }
                                }
                            }
                        }
                    }
                    Scaffold() { padding->
                        Surface(modifier =  Modifier.padding(padding)
                            .background(Color.Gray)) {
                            val pagerState = rememberPagerState(pageCount = {
                                state.value.pageCount
                            }, initialPage = state.value.initialPage)
                            HorizontalPager(state = pagerState) {
                                println(" pager state ${pagerState.currentPage}")
                                if (!pagerState.isScrollInProgress) {
                                    monthViewModel.onPager(pagerState.currentPage)
                                }
                                MonthCalendar(state = state.value, onEvent = { event ->
                                    monthViewModel.onEvent(event)
                                })
                            }
                        }
                    }
                    if( showMessage){
                        println("show message")
                    }
                    if( showAddItemDialog){
                        println("should show add item dialog")
/*                        AddItemDialog(
                            onDismiss = {
                                showAddItemDialog = false
                            },
                            onConfirm ={ item->
                                monthViewModel.onEvent(MonthCalendarEvent.InsertItem(item))
                                showAddItemDialog = false
                            }
                            , settings = monthViewModel.dialogSettings
                        )*/
                    }
                }
            }
        }
    }
}