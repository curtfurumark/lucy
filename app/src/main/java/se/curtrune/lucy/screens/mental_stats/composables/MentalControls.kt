package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.screens.mental_stats.MentalStatsEvent
import se.curtrune.lucy.screens.mental_stats.MentalStatsState
import se.curtrune.lucy.statistics.StatisticsPeriod

@Composable
fun MentalControls(state: MentalStatsState, onEvent: (MentalStatsEvent)->Unit){
    var selectedPeriod by remember {
        mutableStateOf(state.period)
    }
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    var selectedCategory by remember {
        mutableIntStateOf(0)
    }
    var sortedByCategory by remember{
        mutableStateOf(false)
    }
    var sortedByDate by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
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
                        //setPeriod(period)
                        dropdownExpanded = false
                        onEvent(MentalStatsEvent.Period(period))

                    })
            }
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
    }
}

@Composable
@Preview
fun MentalControlsPreview(){
    MentalControls(state = MentalStatsState(), onEvent = {})
}