package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.util.DateTImeFormatter
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ItemSettingsEditor(item: Item, parent: Item?){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()){
        item {  ItemSettingDate(item = item)}
        item {  ItemSettingTime(item = item)}
        item{ ItemSettingCalendar(item = item) }
        item {  ItemSettingRepeat(item = item)}
        item{ItemSettingCategory(item = parent)}
        item{ItemSettingNotification(item = item)}
        item{ItemSettingTags(item = parent)}
        item{ItemSettingColor(item = parent)}
        item{ItemSettingPrioritized(item = item)}
    }
}

@Composable
fun ItemSettingCalendar(item: Item){
    var isCalendarItem by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxWidth().border(Dp.Hairline, color = Color.LightGray)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.is_calender))
            Checkbox(checked = item.isCalenderItem, onCheckedChange = {
                isCalendarItem = !isCalendarItem
            })
        }
    }
}
@Composable
fun ItemSettingCategory(item: Item?){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.category))
        if (item != null) {
            Text(text = item.category)
        }
    }
}
@Composable
fun ItemSettingColor(item: Item?){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.color))
    }
}

@Composable
fun ItemSettingDate(item: Item){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)) {
        Column(
            modifier = Modifier.padding(4.dp)
                .clickable {
                    println("set date")
                })
        {
            Text(text = stringResource(R.string.date))
            if (item.targetDateEpochDay > 0) {
                Text(text = DateTImeFormatter.format(item.targetDate))
            }
        }
    }
}
@Composable
fun ItemSettingDate(date: LocalDate, onEvent: ()->Unit){
     Row(
            modifier = Modifier.padding(8.dp)
                .border(Dp.Hairline, color = Color.LightGray)
                .fillMaxWidth()
                .clickable {
                    println("set date")
                    onEvent()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
                )
        {
            Text(text = stringResource(R.string.date),
                modifier = Modifier.padding(8.dp))
            if( date.toEpochDay() > 0){
                Text(text = DateTImeFormatter.format(date))
            }
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
fun ItemSettingTags(item: Item?){
    Column(modifier = Modifier.padding(4.dp)){
        Text(text = stringResource(R.string.tags))
        if (item != null) {
            Text(text = item.tags)
        }
    }
}

@Composable
fun ItemSettingTime(item: Item){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.time))
            if (item.targetTimeSecondOfDay > 0) {
                Text(text = DateTImeFormatter.format(item.targetTime))
            }
        }
    }
}
@Composable
fun ItemSettingTime(targetTime: LocalTime, onEvent: () -> Unit){
        Row(
            modifier = Modifier.padding(8.dp)
                .fillMaxWidth()
                //.padding(4.dp)
                .clickable { onEvent() }
                .border(Dp.Hairline, color = Color.LightGray),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.time),
                modifier = Modifier.padding(8.dp))
            if (targetTime.toSecondOfDay() > 0) {
                Text(text = DateTImeFormatter.format(targetTime))
            }
        }
}

@Composable
@Preview(showBackground = true)
fun PreviewItemSettingsEditor(){
    val parent = Item("parent")
    parent.category = "dev"
    ItemSettingTime(targetTime = LocalTime.now(), onEvent = {

    })
}

