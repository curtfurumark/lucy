package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.util.DateTImeFormatter
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ItemSettingsEditor(item: Item){
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(state = rememberScrollState())){
        item {  ItemSettingDate(item = item)}
        item {  ItemSettingTime(item = item)}
        item{ ItemSettingCalendar(item = item) }
        item {  ItemSettingRepeat(item = item)}
        item{ItemSettingCategory(item = item)}
        item{ItemSettingNotification(item = item)}
        item{ItemSettingTags(item = item)}
        item{ItemSettingColor(item = item)}
        item{ItemSettingPrioritized(item = item)}
    }
}

@Composable
fun ItemSettingCalendar(item: Item){
    var isCalendarItem by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.is_calender))
        Row(){
            Checkbox(checked = item.isCalenderItem, onCheckedChange = {
                isCalendarItem = !isCalendarItem
            })
        }
    }
}
@Composable
fun ItemSettingCategory(item: Item){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.category))
    }
}
@Composable
fun ItemSettingColor(item: Item){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.color))
    }
}

@Composable
fun ItemSettingDate(item: Item){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.date))
        Text(text = DateTImeFormatter.format(item.targetDate))
    }
}
@Composable
fun ItemSettingNotification(item: Item){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.notification))
    }
}
@Composable
fun ItemSettingPrioritized(item: Item){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.is_prioritized))
    }
}

@Composable
fun ItemSettingRepeat(item: Item){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.repeat))
        Text(text = if( item.hasPeriod()) item.period.toString() else "no repeat")
    }
}
@Composable
fun ItemSettingTags(item: Item){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.tags))
        Text(text = item.tags)
    }
}

@Composable
fun ItemSettingTime(item: Item){
    Column(modifier = Modifier.padding(4.dp)) {
        Text(text = stringResource(R.string.time))
        Text(text = DateTImeFormatter.format(item.targetTime))
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewItemSettingsEditor(){
    ItemSettingsEditor(item = Item())
}

