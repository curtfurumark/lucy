package se.curtrune.lucy.screens.templates.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.classes.Template
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.ItemSettingsRow
import se.curtrune.lucy.composables.add_item.TemplateSettingsRow


@Composable
fun EditTemplateCard(item: Item, onEvent: (EditTemplateEvent) -> Unit){
    var heading by remember { mutableStateOf(item.heading) }
    var showSettings by remember { mutableStateOf(false) }
    val item by remember { mutableStateOf(item) }

    Card(
        modifier = Modifier.fillMaxWidth()){
        Text(text = "id: ${item.id}, time: ${item.targetTime}")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = heading,
                onValueChange = {
                    heading = it
                    item.heading = it
                    onEvent(EditTemplateEvent.Update(item))
                }
            )
            Icon(
                modifier = Modifier.clickable {
                    showSettings = !showSettings
                },
                imageVector = Icons.Default.Edit,
                contentDescription = "edit")
        }
        AnimatedVisibility(visible = showSettings) {
            TemplateSettingsRow(item = item) {
                onEvent(it)
            }
        }
    }
}

@Composable
@Preview
fun PreviewEditTemplateCard(){
    val item = Item("test item")
    val children = listOf(Item("item1"), Item("item2"), Item("item3"))
    item.children?.addAll(children)
    EditTemplateCard(item, onEvent = {})
}