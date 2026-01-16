package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

@Composable
fun ScreensSettings(state: UserState, onEvent: (UserEvent) -> Unit) {
    var showProjects by remember {
        mutableStateOf(state.showProjects)
    }
    var showMedicine by remember {
        mutableStateOf(state.showMedicine)
    }
    var showDuration by remember {
        mutableStateOf(state.showDuration)
    }
    var showDevScreen by remember {
        mutableStateOf(state.showDevScreen)
    }
    var showToDoScreen by remember {
        mutableStateOf(state.showToDo)
    }
    var showMentalStatsScreen by remember {
        mutableStateOf(state.showMentalStats)
    }
    var showAppointments by remember {
        mutableStateOf(state.showAppointments)
    }
    var showTimeLine by remember {
        mutableStateOf(state.showTimeLine)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Text("Screens")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "projects")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showProjects, onCheckedChange = {
                showProjects = it
                onEvent(UserEvent.ShowProjects(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "medicine")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showMedicine, onCheckedChange = {
                showMedicine = it
                onEvent(UserEvent.ShowMedicine(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "duration")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showDuration, onCheckedChange = {
                showDuration = it
                onEvent(UserEvent.ShowDuration(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "dev screen")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showDevScreen, onCheckedChange = {
                showDevScreen = it
                onEvent(UserEvent.ShowDevScreen(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "appointments")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showAppointments, onCheckedChange = {
                showAppointments = it
                //onEvent(UserEvent.ShowDevScreen(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "timeline")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = showTimeLine, onCheckedChange = {
                showTimeLine = it
                onEvent(UserEvent.ShowTimeLine(it))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "todo screen")
            Checkbox(checked = showToDoScreen,
                onCheckedChange = {
                    showToDoScreen = it
                    onEvent(UserEvent.ShowToDo(it))
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "mental stats screen")
            Checkbox(checked = showMentalStatsScreen,
                onCheckedChange = {
                    showMentalStatsScreen = it
                    onEvent(UserEvent.ShowMentalStats(it))
                }
            )
        }
        DevModeSetting(state = state, onEvent = onEvent)
    }
}