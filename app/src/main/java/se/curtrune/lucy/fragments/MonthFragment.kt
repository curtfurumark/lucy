package se.curtrune.lucy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.MonthCalendar
import se.curtrune.lucy.dialogs.EventDialog
import se.curtrune.lucy.viewmodel.LucindaViewModel
import se.curtrune.lucy.viewmodel.MonthViewModel

class MonthFragment : Fragment() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val context = LocalContext.current
                    val monthViewModel = viewModel<MonthViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return MonthViewModel(requireActivity()) as T
                            }
                        }
                    )
                    val mainViewModel = ViewModelProvider(requireActivity())[LucindaViewModel::class.java]
                    val calendarDates by remember {
                        mutableStateOf(monthViewModel.mutableCalendarDates)
                    }
                    val yearMonth by remember {
                        mutableStateOf(monthViewModel.yearMonth)
                    }
                    val pagerState = rememberPagerState(pageCount = {
                        10
                    }, initialPage = 5)
                    HorizontalPager(state = pagerState) {
                        println(" pager state ${pagerState.currentPage}")
                        monthViewModel.onPager(pagerState.currentPage)
                        calendarDates.value?.let {
                            yearMonth.value?.let { it1 ->
                                MonthCalendar(
                                    it,
                                    it1,
                                    onDateClick = { it ->
                                        println(" onCalendarDate click");
                                        if( it.hasEvents()) {
                                            mainViewModel.updateFragment(CalenderDateFragment(it))
                                        }else{
                                            println("add event")
                                            var dialog =
                                                EventDialog(
                                                    it.date
                                                )
                                            dialog.setCallback {
                                                println("new item ${it.heading}")
                                                monthViewModel.addEvent(it)
                                            }
                                            dialog.show(childFragmentManager, "add event")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}