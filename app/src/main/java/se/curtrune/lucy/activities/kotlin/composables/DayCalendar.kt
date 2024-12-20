package se.curtrune.lucy.activities.kotlin.composables

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayCalendar(items: List<Item>){
    Column() {
        Header()
        DaysOfWeek()
        LazyColumn {
            items(items.size) { index ->
                Event(items[index]){
                    println("on event click $it")
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
@Composable
fun DaysOfWeek(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        DayOfWeek.values().iterator().forEach {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = it.getDisplayName(TextStyle.NARROW, Locale.getDefault()))
            }

        }
    }
}

@Composable
fun Event(item: Item, onClick: (String)->Unit){
    var isDone by remember {
        mutableStateOf(item.isDone)
    }
    var targetTime by remember {
        mutableStateOf(item.targetTime)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            println("on row clicked")
        }) {
            Checkbox(checked = isDone, onCheckedChange = {
                isDone = !isDone
            })
            Text(text = item.targetTime.toString(), fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp).clickable {
                println(" on targetTimeClick")
            })
            Text(text = item.heading, fontSize = 18.sp, modifier = Modifier.clickable {
                //onClick(heading)
                println("item clicked: $item")
            }.fillMaxWidth())
        }
    }
}

@Composable
fun Header(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text = LocalDate.now().toString())
    }
}