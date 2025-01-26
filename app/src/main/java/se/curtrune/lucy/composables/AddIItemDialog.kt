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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.util.Converter
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(onDismiss: ()->Unit, onConfirm: (Item)->Unit, settings: ItemSettings){
    println("AddItemDialog() settings date ${settings.targetDate.toString()}")
    println(" target time converted ${Converter.format(settings.targetTime)}")
    var heading by remember{
        mutableStateOf("")
    }
    var targetTime by remember {
        mutableStateOf(settings.targetTime)
    }
    var targetDate by remember {
        mutableStateOf(settings.targetDate)
    }
    var parent by remember {
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
        item.heading = heading
        item.targetDate = targetDate
        item.targetTime = targetTime
        item.setIsCalenderItem(isCalendarItem)
        return item
    }
    Dialog(onDismissRequest = onDismiss){
        val context = LocalContext.current
        Surface(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp)) {
                if(parent != null){
                    Text(text = "add to list ${parent!!.heading}", fontSize = 20.sp)
                }
                OutlinedTextField(
                    value = heading,
                    onValueChange = { heading = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text("heading") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = targetTime.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        showTimeDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = Converter.format(item.targetDate),
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        showDateDialog = true
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "is calendar item $isCalendarItem")
                Spacer(modifier = Modifier.height(8.dp))
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .horizontalScroll(scrollState)){
                    val tmpList = listOf("repeat", "template", "color","category", "is calendar", "notification", "prioritized" )
                    tmpList.forEach {
                        Box(
                            modifier = Modifier.border(Dp.Hairline, color = Color.LightGray)
                                .padding(4.dp)
                                .clickable {
                                    println("on click")
                                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                                }
                        )
                        {
                            Text(text = it)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismiss) {
                        Text(text = "dismiss")
                    }
                    Button(onClick = {
                        onConfirm(getItem())
                    }) {
                        Text(text = "ok")
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