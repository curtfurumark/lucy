package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
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
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.screens.medicine.composable.DropdownItem
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ItemSettingsEditor(item: Item, parent: Item?){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()){
        item {  ItemSettingDate(item = item)}
        item {  ItemSettingTime(item = item)}
        item{ ItemSettingCalendar(item = item, onEvent = {

        }) }
        item {ItemSettingRepeat(item = item)}
        item{ItemSettingCategory(item = parent)}
        item{ItemSettingNotification(item = item, onEvent = {
            println("notification event")
        })}
        item{ItemSettingTags(item = parent, onEvent = {
            println("tags event")
        })}
        item{ItemSettingColor(item = parent, onEvent = {
            println("event color dialog")
        })}
        item{ItemSettingPrioritized(item = item, onEvent = {
            println("event prioritized")
        })}
    }
}
@Composable
fun ItemSettingCalendar(item: Item, onEvent: (Boolean) -> Unit){
    var isCalendarItem by remember {
        mutableStateOf(item.isCalenderItem)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.is_calender))
            Checkbox(checked = isCalendarItem, onCheckedChange = {
                isCalendarItem = !isCalendarItem
                onEvent(isCalendarItem)
            })
        }
    }
}
@Composable
fun ItemSettingAppointment(item: Item, onEvent: (Boolean) -> Unit){
    println("ItemSettingAppointment()is appointment: ${item.isAppointment}")
    var isAppointment by remember {
        mutableStateOf(item.isAppointment)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "appointment")
            Checkbox(checked = isAppointment, onCheckedChange = {
                isAppointment = !isAppointment
                onEvent(isAppointment)
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
fun ItemSettingCategory(item: Item, onEvent: (String) -> Unit){
    var category by remember {
        mutableStateOf(item.category)
    }
    var categoryListExpanded by remember {
        mutableStateOf(false)
    }
    val categories = LucindaApplication.appModule.userSettings.getCategories()
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline,  color = Color.LightGray)
        .clickable {categoryListExpanded = !categoryListExpanded }
        .padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.category))
            //if (item != null) {
            Text(text = category)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "chose category")
            DropdownMenu(expanded = categoryListExpanded, onDismissRequest = {
                categoryListExpanded = false
            }) {
                categories.forEach {
                    DropdownItem(action =it) { cat ->
                        category = cat
                        categoryListExpanded = false
                        onEvent(category)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSettingColor(item: Item?, onEvent: () -> Unit){
    if (item != null) {
        Box(modifier = Modifier.fillMaxWidth()
            .border(Dp.Hairline, color = Color.LightGray)
            .padding(8.dp)
            .background(color = Color(item.color))
            .clickable {
                onEvent()
            }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.color))
            }
        }
    }else{
        Text(text = "item is null")
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
                Text(text = DateTImeConverter.format(item.targetDate))
            }
        }
    }
}
@Composable
fun ItemSettingDate(date: LocalDate, onEvent: ()->Unit){
    println("ItemSettingDate(date: $date)")
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .clickable { onEvent() }) {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = stringResource(R.string.date)
            )
            if (date.toEpochDay() > 0) {
                Text(text = DateTImeConverter.format(date))
            }
        }
    }
}

@Composable
fun ItemSettingLocation(){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)){
        Text(text = "location")
    }
}
@Composable
fun ItemSettingNotification(item: Item, onEvent: () -> Unit){
    if( item.hasNotification()){

    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(8.dp)
        .clickable {
            onEvent()
        }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.notification))
        }
    }
}
@Composable
fun ItemSettingPrioritized(item: Item, onEvent: (Boolean) -> Unit){
    var isPrioritized by remember {
        mutableStateOf(item.isPrioritized)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(4.dp)
        .clickable {
            isPrioritized = !isPrioritized
            onEvent(isPrioritized)
        }) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.is_prioritized))
            Checkbox(checked = isPrioritized, onCheckedChange = {
                isPrioritized = !isPrioritized
                onEvent(isPrioritized)
            })
        }
    }
}

@Composable
fun ItemSettingRepeat(item: Item){
    Column(modifier = Modifier.padding(4.dp)) {
        Text(text = stringResource(R.string.repeat))
        Text(text = if (item.hasRepeat()) item.repeat.toString() else "no repeat")
    }
}
@Composable
fun ItemSettingRepeat(repeat: Repeat?, onEvent: () -> Unit){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .clickable {
        onEvent()
    }) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
                .clickable {
                onEvent()
            }) {
            Text(text = stringResource(R.string.repeat))
            if (repeat != null) {
                Text(text = repeat.toString())
            }
        }
    }
}
@Composable
fun ItemSettingTags(item: Item?, onEvent: () -> Unit){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(8.dp)
        .clickable {
            onEvent()
        }) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = stringResource(R.string.tags))
            if (item != null) {
                Text(text = item.tags)
            }
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
                Text(text = DateTImeConverter.format(item.targetTime))
            }
        }
    }
}
@Composable
fun ItemSettingTime(targetTime: LocalTime, onEvent: () -> Unit){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .clickable { onEvent() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.time)
            )
            if (targetTime.toSecondOfDay() > 0) {
                Text(text = DateTImeConverter.format(targetTime))
            }
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
    val item = Item()
    item.category = "dev"
    ItemSettingCategory(item = Item(), onEvent = {})
}

