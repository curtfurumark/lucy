package se.curtrune.lucy.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTime
import se.curtrune.lucy.composables.top_app_bar.ItemSettingsEditor
import se.curtrune.lucy.util.Converter
import se.curtrune.lucy.util.DateTImeFormatter
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
    val item by remember {
        mutableStateOf(Item())
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var showDateDialog by remember {
        mutableStateOf(false)
    }
    fun getItem(): Item{
        item.parentId = parent?.id ?: 0
        item.heading = heading
        item.targetDate = targetDate
        item.targetTime = targetTime
        item.setIsCalenderItem(isCalendarItem)
        return item
    }
    Dialog(onDismissRequest = onDismiss){
        val context = LocalContext.current
        Surface(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if(parent != null){
                    Text(text = "add to list ${parent!!.heading}", fontSize = 20.sp)
                }else{
                    Text(text = "WARNING PARENT IS NULL", fontSize = 20.sp)
                }
                OutlinedTextField(
                    value = heading,
                    onValueChange = { heading = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("heading") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                //val scrollState = rememberScrollState()
                ItemSettingDate(date = targetDate, onEvent = {
                    showDateDialog = true
                })
                ItemSettingTime(targetTime = targetTime, onEvent = {
                    showTimeDialog = true
                })
                //ItemSettingsEditor(item = item, parent = parent)
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
}

@Composable
@Preview
fun PreviewAddItemDialog(){
    val settings = ItemSettings()
    settings.targetDate = LocalDate.now()
    settings.targetTime = LocalTime.now()
    AddItemDialog(onConfirm = {} , onDismiss = {}, settings = settings)
}