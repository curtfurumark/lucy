package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun ItemWithType(item: Item, onEvent: (ItemEvent)->Unit){
    var currentType by remember{
        mutableStateOf(item.type)
    }
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()
        .padding(4.dp)
        .clickable {
            dropdownExpanded = !dropdownExpanded
        }) {
        Text(text = item.heading)
        Text(text = "type: ${currentType.name}")
        Column() {
            Text(text = currentType.name)
            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = {
                dropdownExpanded = false
            }) {
                Type.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type.name) }, onClick = {
                            println("type chosen: $type")
                            currentType = type
                            item.type = type
                            onEvent(ItemEvent.Update(item))
                            dropdownExpanded = false
                        })
                }
            }
        }
    }
}