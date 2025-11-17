package se.curtrune.lucy.screens.templates.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.TimePickerDialog
import se.curtrune.lucy.screens.templates.create.CreateTemplateEvent
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateTextField(item: Item, onEvent: (CreateTemplateEvent) -> Unit){
    var text by remember { mutableStateOf("") }
    var showContextMenu by remember { mutableStateOf(false) }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var time by remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(start = .16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text= time)
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                value = text,
                onValueChange = {
                    text = it
                    item.heading = it
                    onEvent(CreateTemplateEvent.OnUpdate(item))
                },
                label = { Text("Enter text") }
            )
            IconButton(onClick = { showContextMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("add time") },
                    onClick = {
                        showTimePicker = true
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = {Text("add child")},
                    onClick = {
                        //onEvent(CreateTemplateEvent.AddNewItem(index))
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("delete") },
                    onClick = { /* Handle delete action */ showContextMenu = false })
            }
        }
    }
    if( showTimePicker){
        println("show time picker")
        TimePickerDialog(onDismiss = {
            showTimePicker = false
        }) {
            time = "${it.hour}:${it.minute}"
            val localTime = LocalTime.of(it.hour, it.minute)
            item.targetTime = localTime
            onEvent(CreateTemplateEvent.OnUpdate(item))
            showTimePicker = false
        }
    }
}