package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState
import se.curtrune.lucy.screens.duration.SortedBy
import se.curtrune.lucy.statistics.StatisticsPeriod
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalDate

@Composable
fun DurationControls(state: DurationState, onEvent: (DurationEvent)->Unit){
/*    var fromDate by remember {
        mutableStateOf(LocalDate.now())
    }*/

    //val stats = StatisticsPeriod(fromDate, toDate)
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    var sortedByCategory by remember {
        mutableStateOf(true)
    }
    var sortedByDate by remember {
        mutableStateOf(false)
    }
    var selectedPeriod by remember {
        mutableStateOf(StatisticsPeriod.Companion.Period.DAY)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState())){
            Text(text = stringResource(R.string.from, state.fromDate.toString()))
            Text(text = stringResource(R.string.to, state.toDate.toString()))

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
                            onEvent(DurationEvent.SetPeriod(period))
                            dropdownExpanded = false
                        })
                }
            }
            var selectedCategory by remember {
                mutableIntStateOf(0)
            }
            Row(modifier = Modifier.fillMaxWidth()){
                //val options = listOf("category", "date", "tags")
                val options = listOf("category", "date")
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed{ index, label->
                        SegmentedButton(onClick = {
                            selectedCategory = index
                            when (index) {
                                0 -> {
                                    sortedByCategory = true
                                    onEvent(DurationEvent.SetSortedBy(SortedBy.CATEGORY))
                                    sortedByDate = false
                                }
                                1 -> {
                                    sortedByDate = true
                                    onEvent(DurationEvent.SetSortedBy(SortedBy.DATE))
                                    sortedByCategory = false
                                }
                                else -> {
                                    onEvent(DurationEvent.SetSortedBy(SortedBy.TAGS))
                                }
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "total duration: ${DateTImeConverter.formatSeconds(state.totalDuration)}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                onEvent(DurationEvent.Refresh)
            }){
                Text(text = "refresh")
            }
        }
    }
}