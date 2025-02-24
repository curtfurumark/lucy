package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCalendar
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCategory
import se.curtrune.lucy.composables.top_app_bar.ItemSettingColor
import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate
import se.curtrune.lucy.composables.top_app_bar.ItemSettingNotification
import se.curtrune.lucy.composables.top_app_bar.ItemSettingPrioritized
import se.curtrune.lucy.composables.top_app_bar.ItemSettingRepeat
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTags
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTime
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(onDismiss: ()->Unit, onConfirm: (Item)->Unit, settings: ItemSettings){
    println("AddItemDialog() settings date ${settings.targetDate.toString()}")
    //println(" target time converted ${Converter.format(settings.targetTime)}")
    if( settings.parent == null){
        println("AddItemDialog, ItemSettings.parent is null")
    }

    var heading by remember{
        mutableStateOf("")
    }
    var targetTime by remember {
        mutableStateOf(settings.targetTime)
    }
    var targetDate by remember {
        mutableStateOf(settings.targetDate)
    }
    val parent by remember {
        mutableStateOf(settings.parent)
    }
    var isCalendarItem by remember {
        mutableStateOf(settings.isCalendarItem)
    }
    var category by remember {
        mutableStateOf(settings.parent?.category ?: "")
    }
    val item by remember {
        mutableStateOf(Item())
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var showCategoryDialog by remember {
        mutableStateOf(false)
    }
    var showDateDialog by remember {
        mutableStateOf(false)
    }
    var showRepeatDialog by remember {
        mutableStateOf(false)
    }
    var showTagsDialog by remember {
        mutableStateOf(false)
    }
    var showColorDialog by remember {
        mutableStateOf(false)
    }
    var showNotificationDialog by remember {
        mutableStateOf(false)
    }
    fun getItem(): Item{
        item.parentId = parent?.id ?: 0
        item.heading = heading
        item.targetDate = targetDate
        item.targetTime = targetTime
        item.category = category
        item.setIsCalenderItem(isCalendarItem)
        return item
    }
    Dialog(onDismissRequest = onDismiss){
        Surface(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if(parent != null){
                    Text(text = stringResource(R.string.add_to_list, parent!!.heading), fontSize = 20.sp)
                }else{
                    Text(text = "WARNING PARENT IS NULL", fontSize = 20.sp)
                }
                OutlinedTextField(
                    value = heading,
                    onValueChange = { heading = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.heading)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ItemSettingDate(date = targetDate, onEvent = {
                    showDateDialog = true
                })
                ItemSettingTime(targetTime = targetTime, onEvent = {
                    showTimeDialog = true
                })
                ItemSettingRepeat(item.period, onEvent = {
                    showRepeatDialog = true
                })
                ItemSettingCategory(category, onEvent = {
                    showCategoryDialog = true
                })
                ItemSettingCalendar(item = item, onEvent = { isCalendarItem->
                    item.setIsCalenderItem(isCalendarItem)
                })
                ItemSettingTags(item = item, onEvent = {
                    showTagsDialog = true
                })
                ItemSettingColor(item = item, onEvent = {
                    showColorDialog = true
                })
                ItemSettingNotification(item = item, onEvent = {
                    showNotificationDialog = true
                })
                ItemSettingPrioritized(item = item, onEvent = { isPrioritized ->
                    println("event item: ${item.heading} is prioritized: $isPrioritized")
                })

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        onConfirm(getItem())
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
    if( showCategoryDialog){
        println("showCategoryDialog()")
        //Cate
    }
    if( showTimeDialog){
        TimePickerDialog(
            onDismiss = {
                showTimeDialog = false
        },
            onConfirm = {timePickerState ->
                targetTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                showTimeDialog = false
            })
    }
    if( showDateDialog){
        DatePickerModal( onDismiss = {
            showDateDialog = false
        }, onDateSelected = {
                targetDate = it
        })
    }
    if(showRepeatDialog){
        RepeatDialog(onDismiss = {
            showRepeatDialog = false
        }, onConfirm = { repeat->
            println("on confirm repeat")
            item.setRepeat(repeat)
            showRepeatDialog = false
        },
            repeatIn = item.period)
    }
}

@Composable
@Preview
fun PreviewAddItemDialog(){
    val settings = ItemSettings()
    settings.targetDate = LocalDate.now()
    settings.targetTime = LocalTime.now()
    AddItemDialog(onConfirm = {} , onDismiss = {}, settings = settings)
}