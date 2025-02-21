package se.curtrune.lucy.screens.dev.composables

import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import se.curtrune.lucy.R
import se.curtrune.lucy.util.Converter
import se.curtrune.lucy.statistics.StatisticsPeriod
import se.curtrune.lucy.util.DateTImeFormatter
import java.time.LocalDate

@Composable
fun StatisticsComposable(){
    var fromDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val toDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val stats = StatisticsPeriod(fromDate, toDate)
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
/*    var selectedPeriod by remember {
        mutableStateOf("DAY")
    }*/
    var sortedByCategory by remember {
        mutableStateOf(true)
    }
    var sortedByDate by remember {
        mutableStateOf(false)
    }
    var selectedPeriod by remember {
        mutableStateOf(StatisticsPeriod.Companion.Period.DAY)
    }
    val periods = listOf("DAY", "WEEK", "MONTH", "YEAR")
    fun setPeriod(period: StatisticsPeriod.Companion.Period){
        when(period){
            StatisticsPeriod.Companion.Period.DAY ->{ fromDate = LocalDate.now()}
            StatisticsPeriod.Companion.Period.WEEK ->{
                fromDate = LocalDate.now().minusDays(6)
            }
            StatisticsPeriod.Companion.Period.MONTH ->{
                fromDate = LocalDate.now().minusMonths(1)
            }
            StatisticsPeriod.Companion.Period.YEAR ->{
                fromDate = LocalDate.now().minusYears(1)
            }
        }

    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState())){
            Text(text = stringResource(R.string.from, fromDate.toString()))
            Text(text = stringResource(R.string.to, toDate.toString()))

            Text(
                text = selectedPeriod.name,
                modifier = Modifier.clickable {
                    dropdownExpanded = !dropdownExpanded
                })
            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = {
                dropdownExpanded = false
            }) {
                StatisticsPeriod.Companion.Period.entries.forEach() { period ->
                    DropdownMenuItem(
                        text = { Text(text = period.name) }, onClick = {
                            selectedPeriod = period
                            setPeriod(period)
                            dropdownExpanded = false
                        })
                }
            }
            var selectedCategory by remember {
                mutableIntStateOf(0)
            }
            Row(modifier = Modifier.fillMaxWidth()){
                var options = listOf("category", "date", "tags")

                    SingleChoiceSegmentedButtonRow {
                        options.forEachIndexed{ index, label->
                            SegmentedButton(onClick = {
                                selectedCategory = index
                                if( index == 0){
                                    sortedByCategory = true
                                    sortedByDate = false
                                }else if (index == 1){
                                    sortedByDate = true
                                    sortedByCategory = false
                                }else{
                                    println("tags not implemented")
                                }
                            },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                                selected = index == selectedCategory
                            ) {
                                Text(label)
                            }
                    }

                }
            }
/*            Row(modifier = Modifier.fillMaxWidth()){
                Checkbox(checked = sortedByCategory, onCheckedChange = {
                    sortedByCategory = !sortedByCategory
                    sortedByDate = !sortedByCategory

                })
                Text(text = "sort by category")
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Checkbox(checked = sortedByDate, onCheckedChange = {
                    sortedByDate = !sortedByDate
                    sortedByCategory = !sortedByDate
                })
                Text(text = "sort by date")

            }*/
            Text(text = "estimated duration: ${DateTImeFormatter.formatSeconds(stats.statistics.duration)}")
            Text(text = "actual duration: ${DateTImeFormatter.formatSeconds(stats.statistics.durationActual)}")
            if(sortedByCategory) {
                stats.statistics.groupedByCategory.forEach{ (category, items) ->
                    StatisticsCategory(heading = category, items = items)
                }
            }else {
                stats.statistics.groupedByDate.forEach { (date, items) ->
                    StatisticsDate(date = date, items = items)
                }
            }
        }
    }
}