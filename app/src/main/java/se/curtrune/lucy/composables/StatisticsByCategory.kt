package se.curtrune.lucy.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.Statistics
import java.time.LocalDate

@Deprecated(message = "use Statistics")
@Composable
fun CategoryStatistics(statistics: Statistics){
    val repository = LucindaApplication.repository
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            //statistics = Statistics(repository.selectItems(LocalDate.now().plusDays(1)))
            println("do nothing please")
        }){
            Text(text = "refresh stats")
        }
        Text(text = "today by category")
        Text(text = "estimated duration today: ${Converter.formatSecondsWithHours(statistics.duration)}")
        val categoryMap = statistics.groupedByCategory
        categoryMap.keys.forEach { key->
            val items = categoryMap[key]
            CategoryItem(heading = if(key.isEmpty()) "no category" else key, items = items!!.toList())
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CategoryItem(heading: String, items: List<Item>){
    var itemsVisible by remember{
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()
        .clickable {
            itemsVisible = !itemsVisible
        }){
        val duration = items.sumOf { item -> item.duration }
        Text(text = heading, fontSize =  20.sp)
        Text(text = "duration: ${Converter.formatSecondsWithHours(duration)}")
        AnimatedVisibility(visible =  itemsVisible) {
            Column {
                items.forEach { item->
                    Text(text = "${item.heading} ${Converter.formatSecondsWithHours(item.duration)}",
                        modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}
