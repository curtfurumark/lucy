package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item

@Composable
fun ItemSettingsRow(item: Item, onEdit:(Item)->Unit){
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier.fillMaxWidth()
        .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically){
        ItemSettingDate(item , onDateChanged = { date->
            item.targetDate = date
            onEdit(item)
        })
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingTime(item = item, onTimeChanged = {
            item.targetTime = it
            onEdit
        })
        Spacer(modifier = Modifier.width(8.dp))
/*        ItemSettingRepeat(item = item, onRepeatEvent = {
                repeat->  item.repeat = repeat})*/
       // Spacer(modifier = Modifier.width(8.dp))
        ItemSettingNotification(item = item, onNotication = {
                notification-> item.notification = notification
        })
        Spacer(modifier = Modifier.width(8.dp))
        val categories = LucindaApplication.appModule.userSettings.categories
        ItemSettingCategory(
            item = item, categories = categories,
            onCategoryChanged = {
                category-> item.category = category
            },
            onAddNewCategory = {})
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingAppointment(item = item, onEvent = {
            item.isAppointment = it
        })
        ItemSettingTemplate(item = item, onIsTemplate = { isTemplate ->
            item.isTemplate
        })
    }
}

@Composable
fun ItemSettingsRow2(item: Item, onEdit:(Item)->Unit){
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        //.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically){
        item() {
            ItemSettingDate(item, onDateChanged = { date ->
                item.targetDate = date
            })
        }
        item() {
            Spacer(modifier = Modifier.width(8.dp))
            ItemSettingTime(item = item, onTimeChanged = {
                item.targetTime = it
            })
        }
/*        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingRepeat(item = item, onRepeatEvent = {
                repeat->  item.repeat = repeat})
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingNotification(item = item, onNotication = {
                notification-> item.notification = notification
        })
        Spacer(modifier = Modifier.width(8.dp))
        val categories = LucindaApplication.appModule.userSettings.categories
        ItemSettingCategory(item = item, categories = categories, onEvent = {
                category-> item.category = category
        } )
        Spacer(modifier = Modifier.width(8.dp))
        ItemSettingAppointment(item = item, onEvent = {
            item.setIsAppointment(it)
        })
        ItemSettingTemplate(item = item, onIsTemplate = { isTemplate ->
            item.setIsTemplate(isTemplate)
        })*/
    }
}


@Composable
@PreviewLightDark
fun PreviewItemSettingsRow(){
    LucyTheme {
        ItemSettingsRow(item = Item("hello"), onEdit = {})

    }
}