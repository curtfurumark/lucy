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
import androidx.compose.material3.Card
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
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.ItemSettingCategory
import se.curtrune.lucy.composables.add_item.ItemSettingRepeat
import se.curtrune.lucy.util.DateTImeConverter

@Composable
fun ItemSettingsEditor(item: Item, parent: Item?){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()){
        item {  ItemSettingDate(item = item)}
        item {  ItemSettingTime(item = item)}
        item{ ItemSettingCalendar(item = item, onEvent = {

        }) }
        item {ItemSettingRepeat(item = item, onRepeatEvent = {})}
        //item{ItemSettingCategory(item = parent)}

        item{ItemSettingTags(item = parent, onEvent = {
            println("tags event")
        })}
/*        item{ItemSettingColor(item = parent, onEvent = {
            println("event color dialog")
        })}*/
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
    Card(modifier = Modifier.fillMaxWidth()
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
fun ItemSettingLocation(){
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)){
        Text(text = "location")
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
fun ItemSettingTime(item: Item) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(Dp.Hairline, color = Color.LightGray)
    ) {
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
@Preview(showBackground = true)
fun PreviewItemSettingsEditor(){

}

