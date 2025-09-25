package se.curtrune.lucy.screens.appointments.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.appointments.AppointmentChannel
import se.curtrune.lucy.screens.appointments.AppointmentEvent
import se.curtrune.lucy.screens.appointments.AppointmentsState
import se.curtrune.lucy.screens.appointments.AppointmentsViewModel

@Composable
fun AppointmentsList(
    modifier: Modifier = Modifier,
    state: AppointmentsState,
    onEvent: (AppointmentEvent)->Unit){
    LazyColumn(modifier = modifier.fillMaxWidth().fillMaxWidth()) {
        items(state.items){ item->
            AppointmentItem(appointment = item, onEvent = { event->
                onEvent(event)
            })
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(onEdit: (Item)->Unit){
    println("AppointmentsScreen(onEdit: (Item)->Unit)")
    val viewModel: AppointmentsViewModel = viewModel{
        AppointmentsViewModel.Factory(LucindaApplication.appModule.repository).create(AppointmentsViewModel::class.java)
    }
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    var showAddAppointmentDialog by remember {
        mutableStateOf(false)
    }
    var showMessage by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    fun showMessage(message: String){
        println("show message toast")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    Scaffold(
        floatingActionButton = {
            AddItemFab(onAddClick = {
                showAddAppointmentDialog = true
            })
        },
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

    ) {padding->
        AppointmentsList(modifier = Modifier.padding(padding), state = state, onEvent = {
            viewModel.onEvent(it)
        })
    }
    LaunchedEffect(viewModel) {
        viewModel.eventChannel.collect{ event->
            when(event){
                is AppointmentChannel.EditItem -> {
                    onEdit(event.item)
                }
                is AppointmentChannel.ShowAddItemDialog -> {
                    showAddAppointmentDialog = true
                }
                is AppointmentChannel.ShowMessage -> {
                    showMessage(event.message)
                }
            }
        }
    }
    if( showAddAppointmentDialog){
        AddItemBottomSheet(
            defaultItemSettings = state.defaultItemSettings,
            onDismiss = {
                showAddAppointmentDialog = false
            }, onSave = {
                showAddAppointmentDialog = false
                viewModel.onEvent(AppointmentEvent.InsertAppointment(it))
            })
    }

}