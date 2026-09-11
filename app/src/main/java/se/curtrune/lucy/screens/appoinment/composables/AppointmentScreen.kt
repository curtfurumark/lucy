package se.curtrune.lucy.screens.appoinment.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.ItemSettingDate
import se.curtrune.lucy.composables.add_item.ItemSettingTime
import se.curtrune.lucy.composables.dialogs.AddChildDialog
//import se.curtrune.lucy.composables.top_app_bar.ItemSettingTime
//import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate
import se.curtrune.lucy.screens.appoinment.AppointmentChannel
import se.curtrune.lucy.screens.appoinment.AppointmentEvent
import se.curtrune.lucy.screens.appoinment.AppointmentViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AppointmentScreen(
    appointment: Item,
    modifier: Modifier = Modifier,
    onBack: ()->Unit = {} ,
    navigate: (NavKey)->Unit
) {
    println("AppointmentScreen() appointment: ${appointment.heading}")
    //val viewModel = viewModel<AppointmentViewModel>() {
     //   AppointmentViewModel(appointment)
    //}
    val viewModel: AppointmentViewModel = viewModel(){
        AppointmentViewModel.factory(appointment).create(AppointmentViewModel::class.java)
    }
    //val viewModel = AppointmentViewModel(appointment)
    //LaunchedEffect(Unit) {
    viewModel.onEvent(AppointmentEvent.Refresh(appointment))
    //}
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showAddChildDialog by remember {
        mutableStateOf(false)
    }
    var showAddToTimelineDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            AppointmentFabMenu(
                onEvent = {
                    viewModel.onEvent(it)
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                modifier = Modifier.clickable(
                    onClick = { onBack() }
                ),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back to previous")
            Spacer(modifier = Modifier.height(8.dp))
            HeadingCard(
                item = appointment,
                onHeadingChange = {
                    appointment.heading = it
                    viewModel.onEvent(AppointmentEvent.Update(appointment))
                })
            Spacer(modifier = Modifier.height(4.dp))
            EditDescription(item = appointment, onDescriptionChange = {
                appointment.description = it
                viewModel.onEvent(AppointmentEvent.Update(appointment))
            })
            Spacer(modifier = Modifier.height(4.dp))
            Text(text ="item id: ${appointment.id}")
            Spacer(modifier = Modifier.height(4.dp))
            ItemSettingDate(item = appointment, onDateChanged = {
                appointment.targetDate = it
                viewModel.onEvent(AppointmentEvent.Update(appointment))
            })
            Spacer(modifier = Modifier.height(4.dp))
            ItemSettingTime(item = appointment, onTimeChanged = {
                appointment.targetTime = it
                viewModel.onEvent(AppointmentEvent.Update(appointment))
            })
            Spacer(modifier = Modifier.height(4.dp))
            CheckableItemsCard(
                items = state.children,
                onCheckChange = {
                    viewModel.onEvent(AppointmentEvent.Update(it))
                })
            Spacer(modifier = Modifier.height(4.dp))
            MediaListCard(
                items = state.media,
                onItemClicked = {
                    viewModel.onEvent(AppointmentEvent.ViewFileItem(it))
                },
                onEvent = {viewModel.onEvent(it)})
            //ContactCard(contact = appointment)
            Spacer(modifier = Modifier.height(4.dp))
            SummaryCard(
                item = appointment,
                onSummaryChange = {
                    appointment.comment = it
                    viewModel.onEvent(AppointmentEvent.Update(appointment))
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.channel.collect {
            when(it){
                is AppointmentChannel.Message -> {
                    println(it.message)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is AppointmentChannel.ShowAddChildDialog -> {
                    showAddChildDialog = true
                }

                is AppointmentChannel.Navigate -> {
                    println("navigate to ${it.navKey}")
                    navigate(it.navKey)
                }

                AppointmentChannel.ShowAddToTimeLineDialog -> {


                }
            }
        }
    }
    if( showAddChildDialog){
        AddChildDialog(
            parentId = appointment.id,
            onDismiss = {
                showAddChildDialog = false
            }
        ){
            showAddChildDialog = false
            viewModel.onEvent(AppointmentEvent.AddChild(it))
        }
    }
}

@Composable
@Preview
fun PreviewAppointmentScreen(){
    val item = Item("hjärtmottagning").also {
        it.targetDate = LocalDate.now()
        it.targetTime = LocalTime.now()
        it.description = "ekg"
    }
    AppointmentScreen(appointment = item, navigate = {})
}