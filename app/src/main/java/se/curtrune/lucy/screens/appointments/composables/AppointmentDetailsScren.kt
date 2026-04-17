package se.curtrune.lucy.screens.appointments.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.screens.appointments.AppointmentDetailViewModel

@Composable
fun AppointmentDetailsScreen(appointmentID: Long, modifier: Modifier = Modifier){
    val viewModel=  viewModel{
        AppointmentDetailViewModel(appointmentID)
    }
    val state by viewModel.state.collectAsState()
    var comment by remember {
        mutableStateOf("")
    }
    Scaffold() {paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues)) {
            Text(text = " Appointment Details, id: $appointmentID")
            Text(text = " ${state.item.heading}")
            OutlinedTextField(
                label = {},
                value = comment,
                onValueChange = {
                    comment = it
                }
            )
        }
    }
}