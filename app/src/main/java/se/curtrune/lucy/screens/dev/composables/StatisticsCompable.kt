package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.Statistics
import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

@Composable
fun StatisticsComposable(){
    var fromDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var toDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val stats = StatisticsPeriod(fromDate, toDate)
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    var selectedPeriod by remember {
        mutableStateOf("DAY")
    }
    val periods = listOf("DAY", "WEEK", "MONTH", "YEAR")
    fun setPeriod(period: String){
        when(period){
            "DAY" ->{ println("hello")}
            "WEEK" ->{
                fromDate = LocalDate.now().minusDays(6)
            }
            "MONTH" ->{
                fromDate = LocalDate.now().minusMonths(1)
            }
            "YEAR" ->{
                fromDate = LocalDate.now().minusYears(1)
            }
        }

    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = "from: ${fromDate.toString()}")
            Text(text = "to: ${toDate.toString()}")
            Text(
                text = selectedPeriod,
                modifier = Modifier.clickable {
                    dropdownExpanded = !dropdownExpanded
                })
            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = {
                dropdownExpanded = false
            }) {
                periods.forEach { period ->
                    DropdownMenuItem(
                        text = { Text(text = period) }, onClick = {
                            selectedPeriod = period
                            setPeriod(period)
                            dropdownExpanded = false
                        })
                }
            }
            Text(text = "estimated duration: ${Converter.formatSecondsWithHours(stats.statistics.duration)}")
            Text(text = "actual duration: ${Converter.formatSecondsWithHours(stats.statistics.durationActual)}")
            //Text(text = "from: ${fromDate.toString()}")
            stats.statistics.groupedByCategory.forEach{ (category, items) ->
                StatisticsCategory(heading = category, items = items)
                val duration = items.sumOf { item -> item.duration }
                println("$category ${Converter.formatSecondsWithHours(duration)}")
            }
        }
    }

}