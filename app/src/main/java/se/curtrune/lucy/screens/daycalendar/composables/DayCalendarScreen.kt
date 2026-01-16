package se.curtrune.lucy.screens.daycalendar.composables

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.dialogs.ConfirmDeleteDialog
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.screens.daycalendar.DayCalendarViewModel
import se.curtrune.lucy.screens.daycalendar.DayCalendarChannel
import se.curtrune.lucy.screens.daycalendar.DayCalendarEvent
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCalendarScreen(date: String, navigate: (NavKey) -> Unit, modifier: Modifier = Modifier){
    println("DayCalendarScreen(date: $date, onEdit(Item))")
    val viewModel: DayCalendarViewModel = viewModel(){
        DayCalendarViewModel.factory(LocalDate.parse(date)).create(DayCalendarViewModel::class.java)
    }
    viewModel.onEvent(DayCalendarEvent.CurrentDate(LocalDate.parse(date)))

    val state by viewModel.state.collectAsState()
    var showAddItemDialog by remember{
        mutableStateOf(false)
    }
    var showConfirmDeleteDialog by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            AddItemFab {
                viewModel.onEvent(DayCalendarEvent.ShowAddItemBottomSheet)
            }
        }
    ) { padding->
        CategorizedDayCalendar(modifier = modifier.padding(padding), state = state, onEvent = {
            viewModel.onEvent(it)
        }, onItemEvent = {
            viewModel.onEvent(it)
        })
    }
    if( showAddItemDialog){
        AddItemBottomSheet(
            viewModel.defaultItemSettings,
            onSave = {
                println("onSave $it (DayCalendarScreen)")
                showAddItemDialog = false
                viewModel.onEvent(DayCalendarEvent.AddItem(it))
            },
            onDismiss = {
                println("onDismiss (DayCalendarScreen)")
                showAddItemDialog = false
            }
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect{ event->
            when(event){
                is DayCalendarChannel.ConfirmDeleteDialog -> {
                    showConfirmDeleteDialog = true
                }

                is DayCalendarChannel.ShowAddItemBottomSheet -> {
                    println("channel setting showAddItemDialog to true")
                    showAddItemDialog = true
                }

                is DayCalendarChannel.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is DayCalendarChannel.Navigate -> {
                    navigate(event.navKey)
                }
            }
        }
    }
    if( showConfirmDeleteDialog) {
        ConfirmDeleteDialog(item = state.currentItem!!, onDismiss = {
            showConfirmDeleteDialog = false
        }, onEvent = {
            showConfirmDeleteDialog = false
            viewModel.onEvent(it)
        })
    }
}