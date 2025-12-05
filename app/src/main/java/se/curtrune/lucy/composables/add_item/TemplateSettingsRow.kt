package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.templates.edit.EditTemplateEvent

@Composable
fun TemplateSettingsRow(item: Item, onEvent:(EditTemplateEvent)->Unit){
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier.fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically){
        ItemSettingDate(item , onDateChanged = { date->
            item.targetDate = date
            onEvent(EditTemplateEvent.Update(item))
        })
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingTime(item = item, onTimeChanged = {
            item.targetTime = it
            onEvent(EditTemplateEvent.Update(item))
        })
        Spacer(modifier = Modifier.width(8.dp))
        /*        ItemSettingRepeat(item = item, onRepeatEvent = {
                        repeat->  item.repeat = repeat})*/
        // Spacer(modifier = Modifier.width(8.dp))
        ItemSettingNotification(
            item = item,
            onNotication = {
                item.notification = it
                onEvent(EditTemplateEvent.Update(item))
            })
        Spacer(modifier = Modifier.width(8.dp))
        val categories = LucindaApplication.appModule.userSettings.categories
        ItemSettingCategory(
            item = item, categories = categories,
            onCategoryChanged = {
                item.category = it
                onEvent(EditTemplateEvent.Update(item))

            },
            onAddNewCategory = { category ->
                LucindaApplication.appModule.userSettings.addCategory(category)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingAppointment(item = item, onEvent = {
            item.isAppointment = it
            onEvent(EditTemplateEvent.Update(item))
        })
        Spacer(modifier = Modifier.width(8.dp))
        DeleteItemCard(item = item, onDelete = {
            onEvent(EditTemplateEvent.Delete(it))
        })
    }
}
@Composable
fun DeleteItemCard(item: Item, onDelete:(Item) -> Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Delete ${item.heading}")
            Icon(
                modifier = Modifier.clickable(onClick = {
                    println("on delete icon ${item.heading}")
                    onDelete(item)
                }),
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete")
        }
    }
}