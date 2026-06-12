package se.curtrune.lucy.screens.appointments.composables

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.screens.top_appbar.TopAppbarModule
import se.curtrune.lucy.screens.appointments.AppointmentChannel
import se.curtrune.lucy.screens.appointments.AppointmentEvent
import se.curtrune.lucy.screens.appointments.AppointmentsViewModel
import se.curtrune.lucy.screens.navigation.Route


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    onEdit: (Item)->Unit,
    navigate: (NavKey)-> Unit,
    modifier: Modifier = Modifier
){
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

    ) {padding->
        AppointmentsList(
            modifier = modifier.padding(padding),
            state = state,
            onEvent = {
                viewModel.onEvent(it)
            },
            sortEvent = {
                viewModel.onEvent(it)
            }
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.eventChannel.collect{ event->
            when(event){
                is AppointmentChannel.Navigate -> {
                    navigate(Route.AppointmentScreenNavKey(event.appointment))

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