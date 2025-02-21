package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import se.curtrune.lucy.screens.mental_stats.MentalStatsState

@Composable
fun MentalStatsList(state: MentalStatsState){
    LazyColumn() {
        items(state.stats.statistics.items){ item->
            Text(text = item.heading)
        }

    }

}