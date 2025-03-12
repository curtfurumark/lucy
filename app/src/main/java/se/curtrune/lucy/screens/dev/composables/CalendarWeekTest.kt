package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.persist.Queeries

@Composable
fun CalendarWeekTest(){
    val repository = LucindaApplication.repository
    var result by remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = result)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    val items = repository.selectItems(Queeries.selectAllWeekItems(Week()))
                    result = "${items.size} items found"
                    items.forEach { item ->
                        println(item.toString())
                    }
                }) {
                    Text(text = "get all weekers")
                }
                Button(onClick = {
                    val everything = repository.selectItems(Week())
                    println("found ${everything.size} items")
                    val (allWeek, rest) = everything.partition { it.itemDuration != null }
                    println("allWeek: ${allWeek.size} items")
                    println("rest: ${rest.size} items")
                }) {
                    Text(text = "partition")
                }
                Button(onClick = {
                    val calendarWeek = repository.getCalendarWeek(Week())
                    val allWeekItems = calendarWeek.allWeekItems
                    result = "number of all week items ${allWeekItems.size}"
                }) {
                    Text(text = "get calendar week")
                }
            }
        }
    }
}