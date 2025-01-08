package se.curtrune.lucy.activities.kotlin.monthcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.AddItemDialog
import se.curtrune.lucy.activities.kotlin.composables.AddItemFloatingActionButton

class MonthFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val monthViewModel = viewModel<MonthViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return MonthViewModel(requireActivity()) as T
                            }
                        }
                    )
                    val state = monthViewModel.state.collectAsState()
                    Scaffold(floatingActionButton = {
                        AddItemFloatingActionButton {
                            monthViewModel.onEvent(MonthCalendarEvent.ShowAddItemDialog(true))
                        }
                    }) { padding->
                        Surface(modifier =  Modifier.padding(padding)
                            .background(Color.Gray)) {

                            //val mainViewModel = ViewModelProvider(requireActivity())[LucindaViewModel::class.java]
                            val pagerState = rememberPagerState(pageCount = {
                                10
                            }, initialPage = 5)
                            HorizontalPager(state = pagerState) {
                                println(" pager state ${pagerState.currentPage}")
                                if ( !pagerState.isScrollInProgress){
                                    monthViewModel.onPager(pagerState.currentPage)
                                }
                                MonthCalendar(state = state.value, onEvent = { event ->
                                    monthViewModel.onEvent(event)
                                })
                            }
                            if( state.value.showAddItemDialog){
                                println("should show add item dialog")
                                AddItemDialog(
                                    onDismiss = {
                                    monthViewModel.onEvent(MonthCalendarEvent.ShowAddItemDialog(false))
                                },
                                    onConfirm ={ item->
                                        monthViewModel.onEvent(MonthCalendarEvent.InsertItem(item))
                                        monthViewModel.onEvent(MonthCalendarEvent.ShowAddItemDialog(false))
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}