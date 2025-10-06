package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.composables.TimePickerDialog
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.daycalendar.DayEvent
import se.curtrune.lucy.screens.medicine.composable.DropdownItem
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateItem(
    modifier: Modifier,
    item: Item,
    onEvent: (DayEvent)->Unit){
    var isDone by remember {
        mutableStateOf(item.isDone)
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var targetTime by remember {
        mutableStateOf(item.targetTime)
    }
    var showContextMenu by remember {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    if( showTimePicker){
        println("show time picker")
        TimePickerDialog(onDismiss = {
            showTimePicker = false
        },
            onConfirm ={ timePickerState ->
                targetTime = LocalTime.of(timePickerState.hour , timePickerState.minute)
                item.targetTime = targetTime
                onEvent(DayEvent.UpdateItem(item))
                showTimePicker = false
            } )
    }
    Card(
        modifier = Modifier.fillMaxSize()
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        showContextMenu = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onTap = {
                        println("on tap, i am item ${item.heading}")
                        if( item.hasChild()) {
                            onEvent(DayEvent.ShowChildren(item))
                        }else{
                            onEvent(DayEvent.EditItem(item))
                        }
                    }
                )
            }
            .onSizeChanged {
                itemHeight = with(density){it.height.toDp()}
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(checked = isDone, onCheckedChange = {
                isDone = !isDone
                item.setIsDone(isDone)
                onEvent(DayEvent.UpdateItem(item))
            })
            Text(
                text = DateTImeConverter.format(item.targetTime),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                showTimePicker = true
            })
            Text(
                text = item.heading,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,)
            Spacer(modifier = Modifier.weight(1f))
            if( item.hasChild()) {
                Text(text = ">", modifier = Modifier.padding(end = 4.dp))
            }
        }
        DropdownMenu(
            expanded = showContextMenu,
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            ),
            onDismissRequest ={
                showContextMenu = false
            } ) {
            ContextActions.entries.forEach{
                DropdownItem(it.name, onClick = { action->
                    println("action: $action")
                    when(action){
                        ContextActions.VIEW_STATS.name ->{
                            onEvent(DayEvent.ShowStats(item))
                        }
                        ContextActions.EDIT.name ->{
                            onEvent(DayEvent.EditItem(item))
                        }
                    }
                    showContextMenu = false
                })
            }
        }
    }
}
enum class ContextActions{
    VIEW_STATS, EDIT, DETAILS
}