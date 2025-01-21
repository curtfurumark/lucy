package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.screens.duration.DurationPeriod
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.CategoryListable
import se.curtrune.lucy.statistics.DateListable

@Composable
fun DurationByCategory(){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "test duration stats by category", fontSize = 24.sp)
        Button(onClick = {
            println("on click, give me stats, i need stats, i want stats")
            val statsModule = LucindaApplication.durationStatistics
            statsModule.setPeriod(DurationPeriod.WEEK)
            println("number of categories ${statsModule.itemsByCategory.size}")
            statsModule.itemsByCategory.forEach{ item->
                val categoryItem = item as CategoryListable
                println("category: ${categoryItem.category}")
                println( "duration ${Converter.formatSecondsWithHours(categoryItem.duration)}")
            }
            statsModule.itemsByDate.forEach{ item ->
                val dateItem = item as DateListable
                println("date (heading): ${dateItem.heading}")
                println("duration: ${Converter.formatSecondsWithHours(dateItem.duration)}")
            }
            println("after printing")
        }){
            Text(text = "give me stats")
        }
    }
}