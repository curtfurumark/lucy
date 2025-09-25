package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.HoursMinuteSeconds
import se.curtrune.lucy.composables.add_item.ItemSettingAppointment
import se.curtrune.lucy.composables.add_item.ItemSettingCategory
import se.curtrune.lucy.composables.add_item.ItemSettingDate
import se.curtrune.lucy.composables.add_item.ItemSettingState
import se.curtrune.lucy.composables.add_item.ItemSettingTemplate
import se.curtrune.lucy.composables.add_item.ItemSettingTime
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCalendar
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun ItemEditor(
        modifier: Modifier = Modifier,
        item: Item,
        categories: List<String> = listOf(),
        onEvent: (ItemEvent) -> Unit,
        onSave: (Item) -> Unit) {
    var heading by remember {
        mutableStateOf(item.heading)
    }
    var comment by remember {
        mutableStateOf(item.comment)
    }
    var energy by remember {
        mutableStateOf(5f)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = heading,
            label = { Text(text = "heading") },
            onValueChange = {
                heading = it
                item.heading = it
                onEvent(ItemEvent.Update(item))
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = {
                comment = it
                item.comment = it
                onEvent(ItemEvent.Update(item))
            },
            label = {Text(text = "comment")})
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingDate(item = item, onDateChanged = {
            item.targetDate = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingTime(item = item, onTimeChanged = {
            item.targetTime = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingTemplate(item = item, onIsTemplate = {
            item.setIsTemplate(it)
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingState(item = item, onChange = { done->
            item.state = if (done) State.DONE else State.TODO
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingCalendar(item = item, onEvent = {
            item.setIsCalenderItem(it)
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingCategory(
            item = item,
            categories = categories,
            onCategoryChanged = {
                item.category = it
                onEvent(ItemEvent.Update(item))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        HoursMinuteSeconds(duration = item.duration, onDurationChange = {
            item.duration = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        DurationTimer(
            item = item,
            onDurationChanged = {
                onEvent(ItemEvent.Update(it))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ItemSettingAppointment(item = item, onEvent = {
            item.setIsAppointment(it)
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            onSave(item)
        }){
            Text(text = "save")
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewItemEditorScreen(){
    LucyTheme {
        ItemEditor(item = Item("hello"),
            onEvent = {},
            onSave = {
            println("on event")
        })
        //ItemSettingsRow(item = Item("hello"), onEdit = {})
    }
}
