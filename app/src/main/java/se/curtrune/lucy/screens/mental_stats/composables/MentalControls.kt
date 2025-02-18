package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.mental_stats.MentalStatsState
import se.curtrune.lucy.statistics.StatisticsPeriod

@Composable
fun MentalControls(state: MentalStatsState){
    var selectedPeriod by remember {
        mutableStateOf(StatisticsPeriod.Companion.Period.DAY)
    }
    var dropdownExpanded by remember {
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

                    })
            }
        }
    }
}