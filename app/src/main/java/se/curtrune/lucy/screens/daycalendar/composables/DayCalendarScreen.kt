package se.curtrune.lucy.screens.daycalendar.composables

import MyTopAppBar
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.daycalendar.DayEvent
import se.curtrune.lucy.screens.daycalendar.DateViewModel
import se.curtrune.lucy.screens.daycalendar.DayChannel
import se.curtrune.lucy.composables.AddItemFab
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCalendarScreen(date: String , onEdit: (Item) -> Unit ){
    println("DayCalendarScreen(date: $date, onEdit(Item))")
    val viewModel: DateViewModel = viewModel(){
        DateViewModel.factory(LocalDate.parse(date)).create(DateViewModel::class.java)
    }
    //viewModel.setCurrentDate(LocalDate.parse(date))
    val state by viewModel.state.collectAsState()
    var showAddItemDialog by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect{ event->
            when(event){
                is DayChannel.ConfirmDeleteDialog -> {
                    //showConfirmDeleteDialog = true
                }

                is DayChannel.showAddItemBottomSheet -> {
                    println("channel setting showAddItemDialog to true")
                    showAddItemDialog = true
                }
                is DayChannel.ShowNavigationDrawer -> {

                }

                is DayChannel.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is DayChannel.EditItem -> {
                    //Toast.makeText(context, "edit item", Toast.LENGTH_SHORT).show()
                    onEdit(event.item)
                }
            }
        }
    }

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
                            viewModel.onEvent(appBarEvent)
                        }
                    )
                },
                onEvent = {
                    println("onEvent $it")
                }
            )
        },

        floatingActionButton = {
            AddItemFab {
                println("add item fab clicked")
                viewModel.onEvent(DayEvent.ShowAddItemBottomSheet)
            }
        }
    ) { padding->
        println("padding $padding")
        DayCalendar(modifier = Modifier.padding(padding), state = state, onEvent = {
            viewModel.onEvent(it)
        })
    }
    if( showAddItemDialog){
        AddItemBottomSheet(viewModel.defaultItemSettings, onSave = {
            println("save item")
            showAddItemDialog = false
            viewModel.onEvent(DayEvent.AddItem(it))
        }, onDismiss = {
            showAddItemDialog = false
        })
    }
}