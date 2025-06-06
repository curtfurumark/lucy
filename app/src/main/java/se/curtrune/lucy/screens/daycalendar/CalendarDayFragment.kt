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
import androidx.compose.runtime.Composable
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.ItemStatistics
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.ConfirmDeleteDialog
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.dialogs.ItemStatisticsDialog
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendar
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.util.Logger


class CalendarDayFragment() : Fragment() {
    private val dayViewModel: DateViewModel by viewModels()
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
                    var showAddItemDialog by remember{
                        mutableStateOf(false)
                    }
                    val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
                    //val filter = mainViewModel.filter.collectAsState()
                    val searchFilter = mainViewModel.searchFilter.collectAsState()
/*                    LaunchedEffect(filter.value) {
                        println("launched effect, calendar day filter: ${filter.value} ")
                        //dayViewModel.onEvent(DayEvent.Search(filter.value, true))
                    }*/
                    LaunchedEffect(searchFilter.value) {
                        println("launched effect searchFilter, calendar day filter: ${searchFilter.value} ")
                        dayViewModel.onEvent(DayEvent.Search(searchFilter.value.filter, searchFilter.value.everywhere))
                    }
                    val topAppBarState = mainViewModel.topAppBarState.collectAsState()
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

                                DayChannel.showAddItemBottomSheet -> {
                                    showAddItemDialog = true
                                }

                                is DayChannel.ShowMessage -> {
                                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
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
                            dayViewModel.onEvent(DayEvent.ShowAddItemBottomSheet)
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
                                //val defaultItemSettings = DefaultItemSettings()
                                AddItemBottomSheet(
                                    defaultItemSettings = state.value.defaultItemSettings,
                                    onDismiss = {showAddItemDialog = false},
                                    onSave = {item ->
                                        showAddItemDialog = false
                                        dayViewModel.onEvent(DayEvent.AddItem(item))
                                    },
                                )
                            }
                            if( state.value.editItem){
                                println("edit item please")
                                state.value.editItem = false
                                state.value.currentItem?.let { it1 -> navigate(it1) }
                            }
                            if(state.value.showStats){
                                state.value.currentItem?.let { it1 -> showItemStatisticsDialog(it1) }
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
    @Composable
    private fun Testing(defaultItemSettings: DefaultItemSettings){
        println("testing")

    }

    private fun showItemStatisticsDialog(item: Item) {
        Logger.log("...showItemStatisticsDialog()")
        if (!item.isTemplate) {
            Toast.makeText(context, "not a template", Toast.LENGTH_SHORT).show()
            return
        }
        //TODO
        val repository = LucindaApplication.appModule.repository
        val items = repository.selectTemplateChildren(
            item
        )
        val statistics = ItemStatistics(items)
        val dialog = ItemStatisticsDialog(statistics)
        dialog.show(childFragmentManager, "show statistics")
        Logger.log(statistics)
    }
}