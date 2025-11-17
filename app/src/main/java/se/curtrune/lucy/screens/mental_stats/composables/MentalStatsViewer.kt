package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.mental_stats.MentalDate
import se.curtrune.lucy.screens.mental_stats.MentalStatsState

@Composable
fun MentalStatsViewer(state: MentalStatsState){
    Column(modifier = Modifier.fillMaxSize()){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = state.firstDate.toString())
            Text(text = state.lastDate.toString())
        }
        LazyColumn{
            items(state.mentalDates){
                MentalDateCard(mentalDate = it)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}



@Composable
fun MentalStat(text: String){
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = text)
    }

}

@Composable
@Preview
fun MentalStatsViewerPreview(){
    MentalStatsViewer(state = MentalStatsState())

}