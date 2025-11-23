package se.curtrune.lucy.screens.templates.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.templates.TemplateEvent

@Composable
fun TemplateCard(template: Item, onEvent: (TemplateEvent) -> Unit){
    var showContextMenu by remember { mutableStateOf(false) }
    var showDataDialog by remember { mutableStateOf(false) }
    var showTemplate by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = {
                onEvent(TemplateEvent.OnClick(template))
                showTemplate = !showTemplate
            })){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = template.heading)
            Icon(imageVector = Icons.Default.Add, contentDescription = "add", modifier = Modifier.clickable {
                showContextMenu = true
            })
        }
        AnimatedVisibility(visible = showTemplate) {
            Column(modifier = Modifier.fillMaxWidth()) {
                template.children.forEach { child ->
                    Text(
                        text = child.heading,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        DropdownMenu(
            expanded = showContextMenu,
            onDismissRequest = { showContextMenu = false }
        ) {
            DropdownMenuItem(text = {Text(text = "use this template")},
                onClick = {
                    onEvent(TemplateEvent.ShowUseTemplateDialog(template))
                    showContextMenu = false
                }
            )
            DropdownMenuItem(text = {Text(text = "edit this template")},
                onClick = {
                    onEvent(TemplateEvent.EditTemplate(template))
                    showContextMenu = false
                }
            )
            DropdownMenuItem(text = {Text(text = "delete this template")},
                onClick = {
                    showContextMenu = false
                    onEvent(TemplateEvent.DeleteTemplate(template))
                }
            )
        }
        if( showDataDialog){
            println("showDateDialog")
        }
    }
}
@Composable
@Preview
fun PreviewTemplateCard(){
    val template = Item("test")
    TemplateCard(template = template, onEvent = {})
}