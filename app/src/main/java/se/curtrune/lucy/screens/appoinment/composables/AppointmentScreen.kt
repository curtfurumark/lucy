package se.curtrune.lucy.screens.appoinment.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import se.curtrune.lucy.composables.dialogs.AddChildDialog
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
    val viewModel = viewModel<AppointmentViewModel>() {
        AppointmentViewModel(appointment)
    }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showAddChildDialog by remember {
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
            Icon(
                modifier = Modifier.clickable(
                    onClick = { onBack() }
                ),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back to previous")
            HeadingCard(
                item = appointment,
                onHeadingChange = {
                    appointment.heading = it
                    viewModel.onEvent(AppointmentEvent.Update(appointment))
                })
            Spacer(modifier = Modifier.height(4.dp))
            DateTimeCard(item = appointment)
            Spacer(modifier = Modifier.height(4.dp))
            CheckableItemsCard(
                items = state.children,
                onCheckChange = {
                    viewModel.onEvent(AppointmentEvent.Update(it))
                })
            Spacer(modifier = Modifier.height(4.dp))
            //AddToTimeLineCard()
            //ContactCard(contact = appointment)
            Spacer(modifier = Modifier.height(4.dp))

            EditDescription(item = appointment, onDescriptionChange = {
                appointment.description = it
                viewModel.onEvent(AppointmentEvent.Update(appointment))
            })
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

                AppointmentChannel.ShowAddChildDialog -> {
                    showAddChildDialog = true
                }

                is AppointmentChannel.Navigate -> {
                    println("navigate to ${it.navKey}")
                    navigate(it.navKey)
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
fun DateTimeCard(item: Item){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "date ${item.targetDate.toString()} and time")
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