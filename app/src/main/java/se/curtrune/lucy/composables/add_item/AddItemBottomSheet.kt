package se.curtrune.lucy.composables.add_item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import io.ktor.http.cio.parseMultipart
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.DatePickerModal
import se.curtrune.lucy.composables.RepeatDialog
import se.curtrune.lucy.composables.TimePickerDialog
import se.curtrune.lucy.composables.top_app_bar.ItemSettingAppointment
import se.curtrune.lucy.composables.top_app_bar.ItemSettingCategory
import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate
import se.curtrune.lucy.composables.top_app_bar.ItemSettingRepeat
import se.curtrune.lucy.composables.top_app_bar.ItemSettingTime
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemBottomSheet(
        defaultItemSettings: DefaultItemSettings,
        onDismiss: () -> Unit,
        onSave: (Item) -> Unit
    ){
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    if( defaultItemSettings.parent == null){
        println("parent is null")
    }
    var heading by remember {
        mutableStateOf("") }
    var showRepeatDialog by remember {
        mutableStateOf(false)
    }
    //val parent = defaultItemSettings.parent
    val item by remember {
        mutableStateOf(defaultItemSettings.item)
    }
    var targetDate by remember {
        mutableStateOf(defaultItemSettings.targetDate)
    }
    var targetTime by remember {
        mutableStateOf(defaultItemSettings.targetTime)
    }
    var showDateDialog by remember {
        mutableStateOf(false)
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var isEvent by remember {
        mutableStateOf(defaultItemSettings.isEvent)
    }
    LaunchedEffect(Unit) {
        item.category = defaultItemSettings.parent?.category?: "<no category>"
        println("launched effect, category: ${item.category}")
    }

    ModalBottomSheet(
        onDismissRequest = { /* Handle dismissal */ },
        sheetState = bottomSheetState
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(scrollState)) {
            val parentHeading = defaultItemSettings.parent?.heading?: "parent is null"
            Text(text = parentHeading)
            OutlinedTextField(
                value = heading,
                onValueChange = {
                    heading = it
                    item.heading = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.heading)) },
                supportingText = {
                    if (heading.isBlank()) {
                        Text(
                            text = "heading required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
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
            ItemSettingAppointment(item = item, onEvent = {
                item.setIsAppointment(it)
            })
            AnimatedVisibility(visible = item.isAppointment) {

            }
            ItemSettingIsEvent(isEvent = isEvent, onEvent = { isEvent = it })
            ItemSettingRepeat(item.repeat, onEvent = {
                showRepeatDialog = true
            })
            ItemSettingCategory(item = item, onEvent = {
                category -> item.category = category})
            ItemSettingTemplate(item = item, onEvent = { isTemplate ->
                item.setIsTemplate(isTemplate)
            })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ){
                    Text(text = stringResource(R.string.dismiss))
                }
                Button(
                    onClick = { onSave(item) }){
                    Text(text = stringResource(R.string.add))
                }
            }
        }
        if( showRepeatDialog){
            println("showRepeatDialog")
            RepeatDialog(onDismiss = {
                showRepeatDialog = false
            }, onConfirm = {
                item.repeat = it
                showRepeatDialog = false
            })
        }
        if( showDateDialog){
            DatePickerModal( onDismiss = {
                showDateDialog = false
            }, onDateSelected = { date->
                println("onDateSelected: $date")
                targetDate = date
                item.targetDate = date
                showDateDialog = false
            })
        }
        if( showTimeDialog){
            TimePickerDialog(
                onDismiss = {showTimeDialog = false},
                onConfirm = {timePickerState ->
                    targetTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    item.targetTime = targetTime
                    showTimeDialog = false
                }
            )
        }
    }
}

data class DefaultItemSettings(
    val targetDate: LocalDate = LocalDate.now(),
    val targetTime: LocalTime =  LocalTime.of(0, 0, 0),
    val isTemplate: Boolean = false,
    val isCalendarItem: Boolean = false,
    val parent: Item? = null,
    val item: Item = Item(),
    val isAppointment: Boolean = false,
    val isEvent: Boolean = true

)
@Composable
@Preview
@PreviewLightDark
fun PreviewBottomSheetAddItem(){
    LucyTheme {
        AddItemBottomSheet(defaultItemSettings = DefaultItemSettings(), onDismiss = {}, onSave = {})
    }

}