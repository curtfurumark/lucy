package se.curtrune.lucy.screens.daycalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.ItemStatistics
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.ConfirmDeleteDialog
import se.curtrune.lucy.dialogs.ItemStatisticsDialog
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendar
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.util.Logger
import java.time.LocalTime


class CalendarDayFragment() : Fragment() {
    private var calendarDate: CalenderDate? = null
    init {
        println("CalendarDayFragment init block")
    }

    constructor(calendarDate: CalenderDate) : this() {
        println("CalendarDayFragment(${calendarDate.date.toString()})")
        this.calendarDate = calendarDate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                LucyTheme {
                    // A surface container using the 'background' color from the theme
                    var showAddItemDialog by remember{
                        mutableStateOf(false)
                    }
                    println("before initialization of date view model")
                    //val dayViewModel = viewModel<DateViewModel>()
                    //val dayViewModel = ViewModelProvider(requireActivity())[DateViewModel::class.java]
                    val dayViewModel: DateViewModel = viewModel()
                    println("after initialization of date view model")
                    val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
                    println("after initialization of main view model")
                    val filter = mainViewModel.filter.collectAsState()
                    LaunchedEffect(filter.value) {
                        println("launched effect, calendar day filter: ${filter.value} ")
                        dayViewModel.onEvent(DayEvent.Search(filter.value, true))
                    }
                    if( calendarDate != null){
                        println("calendarDate != null")
                        dayViewModel.setCalendarDate(calendarDate!!)
                    }else{
                        println("calendarDate is null")
                    }
                    val state = dayViewModel.state.collectAsState()
                    val context = LocalContext.current
                    var showConfirmDeleteDialog by remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(dayViewModel) {
                        dayViewModel.eventFlow.collect{ event->
                            when(event){
                                DayChannel.ConfirmDeleteDialog -> {
                                    showConfirmDeleteDialog = true
                                }
                            }
                        }
                    }
                    if( showConfirmDeleteDialog){
                        val item = state.value.currentItem
                        if (item != null) {
                            ConfirmDeleteDialog(item = item, onDismiss = {
                                showConfirmDeleteDialog = false
                            }, onEvent = { event->
                                dayViewModel.onEvent(event)
                                showConfirmDeleteDialog = false
                            })
                        }
                    }
                    Scaffold(
                        floatingActionButton = { AddItemFab {
                            println("add item fab clicked")
                            showAddItemDialog = true
                        }
                        }
                    ) { it ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            DayCalendar(state = state.value, onEvent = { event->
                                println("DayCalendar.onEvent(${event.toString()})")
                                dayViewModel.onEvent(event)
                            })
                            if(showAddItemDialog){
                                AddItemDialog(
                                    onDismiss = {showAddItemDialog = false},
                                    onConfirm = {item ->
                                        showAddItemDialog = false
                                        dayViewModel.onEvent(DayEvent.AddItem(item))
                                    },
                                    settings = DialogSettings(
                                        targetDate = state.value.date,
                                        targetTime = LocalTime.now(),
                                        parent = state.value.currentParent)
                                )
                            }
                            if( state.value.editItem){
                                println("edit item please")
                                state.value.editItem = false
                                state.value.currentItem?.let { it1 -> navigate(it1) }
                            }
                            if(state.value.showStats){
                                state.value.currentItem?.let { it1 -> showItemStatisticsDialog(it1) }
                                //Toast.makeText(context, "statistics doncha just love em", Toast.LENGTH_SHORT).show()
                                state.value.showStats = false
                            }
                        }
                    }
                }

            }
        }
    }
    private fun navigate(item: Item){
        println("navigate")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }

    private fun showItemStatisticsDialog(item: Item) {
        Logger.log("...showItemStatisticsDialog()")
        if (!item.isTemplate) {
            Toast.makeText(context, "not a template", Toast.LENGTH_SHORT).show()
            return
        }
        //TODO
        val repository = LucindaApplication.repository
        val items = repository.selectTemplateChildren(
            item
        )
        val statistics = ItemStatistics(items)
        val dialog = ItemStatisticsDialog(statistics)
        dialog.show(childFragmentManager, "show statistics")
        Logger.log(statistics)
    }
}