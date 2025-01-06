package se.curtrune.lucy.activities.kotlin.daycalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.Week
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale



@Composable
fun DayCalendar(items: List<Item>, onEvent: (DateEvent)->Unit){
    Column() {
        Header()
        DaysOfWeek(Week(), onEvent ={
            println("on date click")
            onEvent(it)
        } )
        LazyColumn {
            items(items.size) { index ->
                Event(items[index], onEvent = {
                    onEvent(it)
                })
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
@Composable
fun DaysOfWeek(week: Week, onEvent: (DateEvent)->Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        var date = week.firstDateOfWeek;
        for(i in 1..7){
            DayOfWeek(date, onEvent = onEvent)
            date = date.plusDays(1)
        }
    }
}
@Composable
fun DayOfWeek(date: LocalDate, onEvent: (DateEvent) -> Unit){
    Column (
        modifier = Modifier.border(1.dp, Color.Blue)
            .clickable { onEvent(DateEvent.CurrentDate(date)) },
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()).uppercase())
        Text(text = date.dayOfMonth.toString())
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Event(item: Item, onEvent: (DateEvent)->Unit){
    var isDone by remember {
        mutableStateOf(item.isDone)
    }
    var targetTime by remember {
        mutableStateOf(item.targetTime)
    }
    var showContextMenu by remember {
        mutableStateOf(false)
    }
    //val haptics = LocalHapticFeedback.current
    Card(modifier = Modifier.fillMaxWidth()){
        Row(
            verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.combinedClickable(
                onLongClick = {
                    //haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    println(" on item card long click ${item.heading}")
                },
                onClick = {
                    println(" on item item  click ${item.heading}")
                    onEvent(DateEvent.EditItem(item))
                }
            )
        ) {
            Checkbox(checked = isDone, onCheckedChange = {
                isDone = !isDone
                item.setIsDone(isDone)
                onEvent(DateEvent.UpdateItem(item))
            })
            Text(text = item.targetTime.toString(), fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp).clickable {
                println(" on targetTimeClick")
                onEvent(DateEvent.EditTime(item))
            })
            Text(text = item.heading, fontSize = 18.sp, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun Header(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text = LocalDate.now().toString())
    }
}