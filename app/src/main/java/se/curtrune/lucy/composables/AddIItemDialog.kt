package se.curtrune.lucy.composables

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.composables.top_app_bar.ItemSettingAppointment
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCalendar
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCategory
import se.curtrune.lucy.composables.top_app_bar.ItemSettingColor
import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate
import se.curtrune.lucy.composables.top_app_bar.ItemSettingLocation
import se.curtrune.lucy.composables.top_app_bar.ItemSettingNotification
import se.curtrune.lucy.composables.top_app_bar.ItemSettingPrioritized
import se.curtrune.lucy.composables.top_app_bar.ItemSettingRepeat
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTags
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTime
import se.curtrune.lucy.screens.medicine.composable.DropdownItem
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(onDismiss: ()->Unit, onConfirm: (Item)->Unit, settings: DialogSettings){
    println("AddItemDialog() settings date ${settings.targetDate.toString()}")
    println("AddItemDialog() settings calender:  ${settings.isCalendarItem}")
    println("AddItemDialog() settings appointment ${settings.isAppointment}")
    if( settings.parent == null){
        println("AddItemDialog, ItemSettings.parent is null")
    }
    val context = LocalContext.current
    var heading by remember{
        mutableStateOf("")
    }
    var isEvent by remember {//has time and date
        mutableStateOf(true)
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

    var category by remember {
        mutableStateOf(settings.parent?.category ?: "")
    }
    val item by remember {
        mutableStateOf(Item())
    }
    if( settings.isAppointment){
        item.type = Type.APPOINTMENT
    }
    if( settings.isCalendarItem){
        item.setIsCalenderItem(true)
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
    LaunchedEffect(true) {
        if( settings.isAppointment){
            println("setting type to APPOINTMENT")
            item.type = Type.APPOINTMENT
        }
        if( settings.isCalendarItem){
            item.setIsCalenderItem(true)
        }
    }
    fun getItem(): Item{
        item.parentId = parent?.id ?: 0
        item.targetDate = targetDate
        item.targetTime = targetTime
        item.category = category
        return item
    }
    Dialog(onDismissRequest = onDismiss){
        val scrollState = rememberScrollState()
        Surface(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(scrollState)) {
                if(parent != null){
                    Text(text = stringResource(R.string.add_to_list, parent!!.heading), fontSize = 20.sp)
                }else{
                    Text(text = "WARNING PARENT IS NULL", fontSize = 20.sp)
                }
                OutlinedTextField(
                    value = heading,
                    onValueChange = {
                        heading = it
                        item.heading = it},
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.heading))},
                    supportingText = {
                        if( heading.isBlank()) {
                            Text(
                                text = "heading required",
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = isEvent) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ItemSettingDate(date = targetDate, onEvent = {
                            showDateDialog = true
                        })
                        ItemSettingTime(targetTime = targetTime, onEvent = {
                            showTimeDialog = true
                        })
                    }
                }
                ItemSettingNoTime(onEvent = {
                    isEvent = it
                    //TODO
                })
                ItemSettingDuration(item= item, onEvent = { itemDuration->
                    println("duration type: ${itemDuration.type.name}")
                    item.itemDuration = itemDuration
                })
                ItemSettingRepeat(item.repeat, onEvent = {
                    showRepeatDialog = true
                })
                ItemSettingCategory(item = item, onEvent = { category->
                    item.category = category
                })
                ItemSettingCalendar(item = item, onEvent = { isCalendarItem->
                    println(" setting is calendar item to $isCalendarItem")
                    item.setIsCalenderItem(isCalendarItem)
                })
                ItemSettingAppointment(item = item) { isAppointment->
                    if( isAppointment) {
                        item.type = Type.APPOINTMENT
                    }else{
                        item.type = Type.NODE
                    }
                }
                ItemSettingPrioritized(item = item, onEvent = { isPrioritized ->
                    println("event item: ${item.heading} is prioritized: $isPrioritized")
                })
                ItemSettingTemplate(item = item, onEvent = { isTemplate ->
                    item.setIsTemplate(isTemplate)
                })
                ItemSettingSyncWithGoogle(item = item, onEvent = {
                    println("sync with google calendar")
                })
                if( item.type == Type.APPOINTMENT){
                    ItemSettingLocation()
                }
                ItemSettingTags(item = item, onEvent = {
                    showTagsDialog = true
                })
                ItemSettingColor(item = item, onEvent = {
                    showColorDialog = true
                })

                ItemSettingNotification(item = item, onEvent = {
                    showNotificationDialog = true
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
                        if( heading.isNotBlank()) {
                            onConfirm(getItem())
                        }else{
                            println("missing heading")
                           Toast.makeText(context, context.resources.getString(R.string.missing_heading), Toast.LENGTH_LONG).show()
                        }
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
    if( showCategoryDialog){
        CategoryDialog(category = item.category, dismiss= {
            println("category dialog dismiss")
            showCategoryDialog = false
        }, onCategory = {
            println("...onCategory: $it")
            item.category = it
            showCategoryDialog = false
        })
        //Cate
    }
    if( showTimeDialog){
        TimePickerDialog(
            onDismiss = {
                showTimeDialog = false
        },
            onConfirm = {timePickerState ->
                targetTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                item.targetTime = targetTime
                showTimeDialog = false
            })
    }
    if( showDateDialog){
        DatePickerModal( onDismiss = {
            showDateDialog = false
        }, onDateSelected = {
                targetDate = it
            item.targetDate = it
        })
    }
    if( showColorDialog){
        ColorPicker(dismiss = {
            showColorDialog = false
        }, onColor = { color ->
            item.color = color.toArgb()
            showColorDialog = false
        })
    }
    if(showRepeatDialog){
        RepeatDialog(onDismiss = {
            showRepeatDialog = false
        }, onConfirm = { repeat->
            println("on confirm repeat")
            //item.setRepeat(repeat)
            showRepeatDialog = false
        })
    }
    if( showTagsDialog){
        TagsDialog(onDismiss = {
            showTagsDialog = false
        }, onConfirm = { tags ->
            showTagsDialog = false
            item.tags = tags
        })
    }
    if(showNotificationDialog){
        NotificationDialog(onDismiss = {
            showNotificationDialog = false
        }, onConfirm = { notification->
            item.notification = notification

        })
    }
}

@Composable
fun ItemSettingDuration(item: Item ,onEvent: (ItemDuration)->Unit){
    var showDropdown by remember {
        mutableStateOf(false)
    }
    var itemDuration by remember {
        mutableStateOf(ItemDuration())
    }
    var itemDurationType by remember {
        mutableStateOf(item.itemDuration?.type?: ItemDuration.Type.SECONDS)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(Dp.Hairline, color = Color.LightGray)
            .clickable {
                showDropdown = !showDropdown
            }){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = stringResource(R.string.duration))
            Text(text = itemDurationType.name)
            DropdownMenu(expanded = showDropdown, onDismissRequest = {
                showDropdown = false
            }) {
                ItemDuration.Type.entries.forEach { durationType->
                    DropdownItem(action = durationType.name) {
                        itemDuration.type = durationType
                        itemDurationType = durationType
                        showDropdown = false
                        onEvent(itemDuration)
                    }
                }
            }
        }

    }
}
@Composable
fun ItemSettingSyncWithGoogle(item: Item, onEvent: (Boolean)->Unit){
    var syncWithGoogle by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.add_to_google_cal))
            Checkbox(checked = syncWithGoogle, onCheckedChange = {
                syncWithGoogle = !syncWithGoogle
                onEvent(syncWithGoogle)
            })
        }
    }
}
@Composable
fun ItemSettingTemplate(item: Item, onEvent: (Boolean) -> Unit){
    var isTemplate by remember {
        mutableStateOf(item.isTemplate)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.is_template))
            Checkbox(checked = isTemplate, onCheckedChange = {
                isTemplate = !isTemplate
                onEvent(isTemplate)
            })
        }
    }
}
@Composable
fun ItemSettingNoTime(onEvent:(Boolean)->Unit){
    var timedItem by remember {
        mutableStateOf(true)
    }
    Box(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.timed_item))
            Checkbox(checked = timedItem, onCheckedChange = {
                timedItem = !timedItem
                onEvent(timedItem)
            })
        }
    }
}

@Composable
@Preview
fun PreviewAddItemDialog(){
    val settings = DialogSettings()
    settings.targetDate = LocalDate.now()
    settings.targetTime = LocalTime.now()
    AddItemDialog(onConfirm = {} , onDismiss = {}, settings = settings)
}