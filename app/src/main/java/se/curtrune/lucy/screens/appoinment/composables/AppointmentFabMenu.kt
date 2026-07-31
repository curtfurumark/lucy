package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onVisibilityChangedNode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.curtrune.lucy.screens.appoinment.AppointmentEvent


@Composable
fun AppointmentFabMenu(onEvent: (AppointmentFabMenuEvent)->Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = {
                    expanded = it
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")

            }
        }
    ){
        items.forEach { item->
            FloatingActionButtonMenuItem(
                text = { Text(item.text) },
                onClick = {
                    expanded = false
                    onEvent(item.event)
                    println("clicked ${item.text}")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                }
            )
        }
    }
}

val items = listOf(
    FabMenuItem("add image", event = AppointmentFabMenuEvent.AddImage),
    FabMenuItem("attach file", event = AppointmentFabMenuEvent.AttachFile),
    FabMenuItem("add contact", event = AppointmentFabMenuEvent.AddContact),
    FabMenuItem("add voice recording", event = AppointmentFabMenuEvent.AddVoiceRecording),
    FabMenuItem("add to timeline", event = AppointmentFabMenuEvent.AddToTimeLine),
    FabMenuItem("add checkable note", event = AppointmentFabMenuEvent.AddCheckableNote),
    FabMenuItem("add script", event = AppointmentFabMenuEvent.AddScript),
)



data class FabMenuItem(
    val text: String,
    val icon: Int = 0,
    val event: AppointmentFabMenuEvent = AppointmentFabMenuEvent.AttachFile
)

sealed interface AppointmentFabMenuEvent{
    data object AddContact: AppointmentFabMenuEvent
    data object AddImage: AppointmentFabMenuEvent
    data object AddScript: AppointmentFabMenuEvent
    data object AddVoiceRecording: AppointmentFabMenuEvent
    data object AddToTimeLine: AppointmentFabMenuEvent
    data object AttachFile: AppointmentFabMenuEvent
    data object AddCheckableNote: AppointmentFabMenuEvent
    data object Pending: AppointmentFabMenuEvent
}
