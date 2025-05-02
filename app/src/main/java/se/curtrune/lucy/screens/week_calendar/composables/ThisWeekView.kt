package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.calender.CalendarWeek
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.medicine.composable.DropdownItem
import se.curtrune.lucy.screens.week_calendar.WeekEvent
import se.curtrune.lucy.screens.week_calendar.WeekState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThisWeekView(state: WeekState, onEvent: (WeekEvent) -> Unit){
    println("ThisWeekView, number of items: ${state.calendarWeek.allWeekItems.size}")
    var showContextMenu by remember {
        mutableStateOf(state.showContextMenu)
    }
    Box(
        modifier = Modifier
            .border(2.dp, color = Color.DarkGray)
            .aspectRatio(1.2F)
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .combinedClickable(
                onLongClick = {
                    onEvent(WeekEvent.OnAllWeekLongClick(state.currentWeek))
                },
                onClick = {
                    onEvent(WeekEvent.OnAllWeekClick(state.currentWeek))
                }
            ),
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier.padding(4.dp)
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Icon(
                    //modifier = Modifier.align(Alignment.TopEnd),
                    imageVector = Icons.Default.Add,
                    contentDescription = "add note")
                state.calendarWeek.allWeekItems.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .border(Dp.Hairline, color = Color.Black)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = item.heading,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            maxLines = 1
                        )
                    }
                }
        }
    }
    if(showContextMenu){
        val options = listOf("add note")
        DropdownMenu(onDismissRequest = {
            showContextMenu = false
        }, expanded = true) {
            options.forEach{ action->
                DropdownItem(action = action ) { }
            }
        }
    }
}
@Composable
fun AllWeekRow(item: Item, onEvent: (WeekEvent) -> Unit){
    Text(text = item.heading)
}

@Composable
@Preview
fun PreviewThisWeek(){
    LucyTheme {
        val items = listOf(Item("påsk"), Item("barnvecka"), Item("hej då"))
        val calendarWeek = CalendarWeek()
        calendarWeek.allWeekItems = items
        val weekState = WeekState(
            currentWeek = Week(),
            calendarWeek = calendarWeek,
        )
        ThisWeekView(weekState, onEvent = {})
    }
}